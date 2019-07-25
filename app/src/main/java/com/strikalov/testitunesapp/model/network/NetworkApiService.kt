package com.strikalov.testitunesapp.model.network

import com.strikalov.testitunesapp.model.network.networkentity.ResultCollectionJson
import com.strikalov.testitunesapp.model.network.networkentity.ResultTrackJson
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApiService {

    @GET("search")
    fun downloadCollectionResult(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "album"
    ): Observable<ResultCollectionJson>

    @GET("lookup")
    fun downloadTrackResult(
        @Query("id") id: String,
        @Query("entity") entity: String = "song"
    ): Observable<ResultTrackJson>

}