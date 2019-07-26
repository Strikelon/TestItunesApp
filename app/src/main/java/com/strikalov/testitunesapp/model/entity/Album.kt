package com.strikalov.testitunesapp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Сущность музыкальный альбом
 */
@Parcelize
data class Album(

    //id альбома, для запроса песен по id
    val albumId: String,

    //Название альбома
    val albumName: String,

    //Имя исполнителя
    val artistName: String?,

    //Дата релиза
    val releaseYear: String?,

    //Ссылка на картинку для альбома
    val pictureUrl: String?,

    //В каком жанре музыка
    val primaryGenreName: String?,

    //Количество треков в альбоме
    val trackCount: String?,

    //Стоимость альбома
    val albumPrice: String?,

    //Валюта стоимости альбома
    val currency: String?


) : Comparable<Album>, Parcelable {

    //Метод сравнения, чтобы можно было сортировать альбомы в алфавитном порядке
    //по названию альбома
    override fun compareTo(other: Album): Int = albumName.compareTo(other.albumName)
}