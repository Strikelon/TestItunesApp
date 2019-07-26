package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Класс представляет список музыкальных альбомов, возвращаемый сервером,
 * обьекты этого класса используются для преобразования в него
 * Json файлов, предоставленных сервером
 */
data class ResultCollectionJson(

    @Expose
    @SerializedName("results")
    val results: List<CollectionJson>
)

