package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Класс представляет список музыкальных трэков ,возвращаемый сервером,
 * обьекты этого класса используются для преобразования в него
 * Json файлов, предоставленных сервером
 */
data class ResultTrackJson (

    @Expose
    @SerializedName("results")
    val results: List<TrackJson>
)