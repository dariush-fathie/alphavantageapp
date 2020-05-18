package com.alphavantage.app

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.alphavantage.app.data.di.dataModules
import com.alphavantage.app.data.local.ObjectBox
import com.alphavantage.app.di.uiModules
import com.alphavantage.app.di.viewModelModules
import com.alphavantage.app.domain.di.domainModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
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
            modules(dataModules + domainModules + uiModules + viewModelModules)
        }
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}