package com.strikalov.testitunesapp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(

    val albumId: String,

    val albumName: String,

    val artistName: String?,

    val releaseYear: String?,

    val pictureUrl: String?,

    val primaryGenreName: String?,

    val trackCount: String?,

    val albumPrice: String?,

    val currency: String?


) : Comparable<Album>, Parcelable {

    override fun compareTo(other: Album): Int = albumName.compareTo(other.albumName)
}