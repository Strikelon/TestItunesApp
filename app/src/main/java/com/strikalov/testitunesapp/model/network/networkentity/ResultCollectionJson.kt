package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultCollectionJson(

    @Expose
    @SerializedName("results")
    val results: List<CollectionJson>
)

