package com.strikalov.testitunesapp.model.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkApi{

    companion object {
        private const val BASE_URL = "https://itunes.apple.com"

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

    fun downloadCollectionResult(term: String) = networkApiService.downloadCollectionResult(term)

    fun downloadTrackResult(id: String) = networkApiService.downloadTrackResult(id)

}