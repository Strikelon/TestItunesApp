package com.strikalov.testitunesapp.model.network.networkentity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Класс представляет музыкальный трэк, обьекты этого класса используются для преобразования в него
 * Json файлов, предоставленных сервером
 */
data class TrackJson(

    @Expose
    @SerializedName("wrapperType")
    /**
     * В приложении это поле используется, т.к. при запросе музыкальных трэков
     * в json файле кроме трэков, если еще json обьект, представляющий музыкальный альбом,
     * чтобы отсеять музыкальный альбом при обработке, мы просматриваем значение этого поля
     */
    val wrapperType: String,

    @Expose
    @SerializedName("trackName")
    //Название музыкального трэка
    val trackName: String,

    @Expose
    @SerializedName("previewUrl")
    //Ссылка на демо-версию трэка для прослушивания
    val previewUrl: String,

    @Expose
    @SerializedName("trackPrice")
    //Стоимость трэка
    val trackPrice: String,

    @Expose
    @SerializedName("currency")
    //Валюта стоимости
    val currency: String

)