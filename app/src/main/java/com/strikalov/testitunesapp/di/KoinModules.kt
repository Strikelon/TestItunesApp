package com.strikalov.testitunesapp.di

import com.strikalov.testitunesapp.model.interactors.AlbumInteractor
import com.strikalov.testitunesapp.model.interactors.AlbumInteractorImpl
import com.strikalov.testitunesapp.model.interactors.TrackInteractor
import com.strikalov.testitunesapp.model.interactors.TrackInteractorImpl
import com.strikalov.testitunesapp.model.network.NetworkApi
import com.strikalov.testitunesapp.model.repositories.NetworkRepository
import com.strikalov.testitunesapp.model.repositories.NetworkRepositoryImpl
import com.strikalov.testitunesapp.viewmodel.DetailViewModel
import com.strikalov.testitunesapp.viewmodel.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Создаем модуль для инъекции зависимостей с помощью библиотеки Koin
 */
val appModule = module {

    //Singletons
    single { NetworkApi() }
    single<NetworkRepository> { NetworkRepositoryImpl(get()) }
    single<AlbumInteractor> { AlbumInteractorImpl(get()) }
    single<TrackInteractor> { TrackInteractorImpl(get()) }

    //ViewModels
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}