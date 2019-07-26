package com.strikalov.testitunesapp.model.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Класс для работы с апи Itunes
 */
class NetworkApi{

    companion object {

        //Базовая ссылка
        private const val BASE_URL = "https://itunes.apple.com"

        //Константа, чтобы при обработке списка музыкальных трэков из json-файла отсеять
        //json обьект представляющий музыкальный альбом
        const val TRACK_WRAPPER = "track"
    }

    private var networkApiService: NetworkApiService

    init {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        networkApiService = retrofit.create(NetworkApiService::class.java)
    }

    /**
     * Метод возвращает список музыкальных альбомов полученных от сервера
     */
    fun downloadCollectionResult(term: String) = networkApiService.downloadCollectionResult(term)

    /**
     * Метод возвращает список музыкальных трэков полученных от сервера
     */
    fun downloadTrackResult(id: String) = networkApiService.downloadTrackResult(id)

}