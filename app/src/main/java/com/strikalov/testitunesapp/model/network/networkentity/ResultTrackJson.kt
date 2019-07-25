package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultTrackJson (

    @Expose
    @SerializedName("results")
    val results: List<TrackJson>
)