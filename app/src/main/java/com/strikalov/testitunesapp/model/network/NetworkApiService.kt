package com.strikalov.testitunesapp.model.network

import com.strikalov.testitunesapp.model.network.networkentity.ResultCollectionJson
import com.strikalov.testitunesapp.model.network.networkentity.ResultTrackJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApiService {

    /**
     * Создаем get-запрос на сервер, чтобы получить список музыкальных альбомов
     * Пример запроса:
     * https://itunes.apple.com/search?term=jack+johnson&media=music&entity=album
     */
    @GET("search")
    fun downloadCollectionResult(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album"
    ): Observable<ResultCollectionJson>

    /**
     * Создаем get-запрос на сервер, чтобы получить список музыкальных трэков по id альбома
     * Пример запроса:
     * https://itunes.apple.com/lookup?id= 129994297&entity=song
     */
    @GET("lookup")
    fun downloadTrackResult(
        @Query("id") id: String,
        @Query("entity") entity: String = "song"
    ): Observable<ResultTrackJson>

}