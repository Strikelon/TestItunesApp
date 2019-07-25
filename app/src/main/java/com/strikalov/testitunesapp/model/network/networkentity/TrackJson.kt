package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TrackJson(

    @Expose
    @SerializedName("wrapperType")
    val wrapperType: String,

    @Expose
    @SerializedName("trackName")
    val trackName: String,

    @Expose
    @SerializedName("previewUrl")
    val previewUrl: String,

    @Expose
    @SerializedName("trackPrice")
    val trackPrice: String,

    @Expose
    @SerializedName("currency")
    val currency: String

)