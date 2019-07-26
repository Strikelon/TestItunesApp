package com.strikalov.testitunesapp

import android.app.Application
import com.strikalov.testitunesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Класс приложения
 */
class App : Application(){

    override fun onCreate() {
        super.onCreate()

        /**
         * Запускаем модуль для инъекции зависимостей с помощью библиотеки Koin
         */
        startKoin {

            androidContext(this@App)

            modules(appModule)

        }
    }
}