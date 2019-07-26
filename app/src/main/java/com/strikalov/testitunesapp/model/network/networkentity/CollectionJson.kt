package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Класс представляет музыкальный альбом, обьекты этого класса используются для преобразования в него
 * Json файлов, предоставленных сервером
 */
data class CollectionJson(

    @Expose
    @SerializedName("collectionId")
    //Id музыкального альбома
    val collectionId: String,

    @Expose
    @SerializedName("collectionName")
    //Название музыкального альбома
    val collectionName: String,

    @Expose
    @SerializedName("artistName")
    //Имя исполнителя
    val artistName: String,

    @Expose
    @SerializedName("releaseDate")
    //Дата релиза альбома
    val releaseDate: String,

    @Expose
    @SerializedName("artworkUrl100")
    //Ссылка на картинку альбома
    val artworkUrl100: String,

    @Expose
    @SerializedName("primaryGenreName")
    //Жанр музыки
    val primaryGenreName: String,

    @Expose
    @SerializedName("trackCount")
    //Количество музыкальных трэков
    val trackCount: String,

    @Expose
    @SerializedName("collectionPrice")
    //Стоимость музыкального альбома
    val collectionPrice: String,

    @Expose
    @SerializedName("currency")
    //Валюта стоимости
    val currency: String

)