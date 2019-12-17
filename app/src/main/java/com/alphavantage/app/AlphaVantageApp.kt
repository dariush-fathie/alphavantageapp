package com.alphavantage.app

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.alphavantage.app.data.di.dataModules
import com.alphavantage.app.data.local.ObjectBox
import com.alphavantage.app.di.viewModelModules
import com.alphavantage.app.domain.di.domainModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import timber.log.Timber

class AlphaVantageApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        ObjectBox.init(this)
        startKoin {
            androidContext(this@AlphaVantageApp)
            modules(dataModules + domainModules + viewModelModules)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}