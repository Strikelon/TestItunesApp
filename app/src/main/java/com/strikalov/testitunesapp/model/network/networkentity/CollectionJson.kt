package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CollectionJson(

    @Expose
    @SerializedName("collectionId")
    val collectionId: String,

    @Expose
    @SerializedName("collectionName")
    val collectionName: String,

    @Expose
    @SerializedName("artistName")
    val artistName: String,

    @Expose
    @SerializedName("releaseDate")
    val releaseDate: String,

    @Expose
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,

    @Expose
    @SerializedName("primaryGenreName")
    val primaryGenreName: String,

    @Expose
    @SerializedName("trackCount")
    val trackCount: String,

    @Expose
    @SerializedName("collectionPrice")
    val collectionPrice: String,

    @Expose
    @SerializedName("currency")
    val currency: String

)