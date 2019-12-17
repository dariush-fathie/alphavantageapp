package com.alphavantage.app.data.di

import android.content.Context
import android.os.Looper
import com.alphavantage.app.data.BuildConfig
import com.alphavantage.app.data.remote.ApiConstants
import com.alphavantage.app.domain.util.NetworkUtils
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

@Deprecated("Sementara tidak terpakai")
fun provideContext(context: Context): Context = context.applicationContext

@Deprecated("Sementara tidak terpakai")
fun provideCache(context: Context): Cache {
    if (Looper.getMainLooper() == Looper.myLooper())
        throw IllegalStateException("Initializing cache on main thread")

    val file = File(context.cacheDir, "av_cache")
    return Cache(file, ApiConstants.CACHE_SIZE)
}

@Deprecated("Sementara tidak terpakai")
fun provideHttpClient(context: Context, cache: Cache): OkHttpClient {
    if (Looper.getMainLooper() == Looper.myLooper())
        throw IllegalStateException("Initializing cache on main thread")

    return OkHttpClient.Builder()
        .connectTimeout(ApiConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(ApiConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(ApiConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .cache(cache)
        .addInterceptor { chain ->
            var request = chain.request()
            // TODO put API key in NDK
            val url = when (request.url.host) {
                "www.alphavantage.co" -> request.url.newBuilder().addQueryParameter(
                    "apikey",
                    "FS0FJ420A52OG88R"
                ).build()
                else -> request.url.newBuilder().build()
            }

            request = if (NetworkUtils.hasNetwork(context))
                request.newBuilder().header(
                    "Cache-Control",
                    "public, max-age=" + 5
                ).url(url).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).url(url).build()

            chain.proceed(request)
        }
        .build()
}

fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(ApiConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(ApiConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(ApiConstants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            var request = chain.request()
            // TODO put API key in NDK
            val url = when (request.url.host) {
                "www.alphavantage.co" -> request.url.newBuilder().addQueryParameter(
                    "apikey",
                    "FS0FJ420A52OG88R"
                ).build()
                else -> request.url.newBuilder().build()
            }

            request = request.newBuilder().header(
                "Cache-Control",
                "public, max-age=" + 5
            ).url(url).build()

            chain.proceed(request)
        }
        .build()
}

fun provideTypeAdapter(): TypeAdapterFactory {
    return object : TypeAdapterFactory {

        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
            val delegate = gson!!.getDelegateAdapter(this, type)
            val elementAdapter = gson!!.getAdapter(JsonElement::class.java)

            return object : TypeAdapter<T>() {

                override fun write(out: JsonWriter?, value: T) {
                    delegate.write(out, value)
                }

                override fun read(`in`: JsonReader?): T {
                    val jsonElement = elementAdapter.read(`in`)
                    if (jsonElement.isJsonObject) {
                        val jsonObject = jsonElement.asJsonObject
                        if (jsonObject.has("Error Message"))
                            throw Exception(jsonObject.get("Error Message").asString)

                        if (jsonObject.has("Information"))
                            throw Exception(jsonObject.get("Information").asString)

                        if (jsonObject.has("Note"))
                            throw Exception(jsonObject.get("Note").asString)
                    }

                    return delegate.fromJsonTree(jsonElement)
                }
            }.nullSafe()
        }
    }
}

fun provideGson(typeAdapterFactory: TypeAdapterFactory): Gson {
    return GsonBuilder()
        .registerTypeAdapterFactory(typeAdapterFactory)
        .create()
}

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_API_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

val networkModule = module {
//    factory { provideContext(get()) }
//    factory { provideCache(get()) }
    factory { provideHttpClient() }
    factory { provideTypeAdapter() }
    factory { provideGson(get()) }
    single { provideRetrofit(get(), get()) }
}