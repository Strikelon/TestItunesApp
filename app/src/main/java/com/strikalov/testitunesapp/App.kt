package com.strikalov.testitunesapp

import android.app.Application
import com.strikalov.testitunesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@App)

            modules(appModule)

        }
    }
}