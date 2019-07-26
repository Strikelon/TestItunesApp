package com.strikalov.testitunesapp.model.entity

/**
 * Сущность музыкальный трэк
 */
data class Track (

    //Название трэка
    val trackName: String?,

    //Ссылка на демо-версию трэка для проигрывания
    val trackUrl: String?,

    //Стоимость трэка
    val trackPrice: String?,

    //Валюта стоимости
    val currency: String?,

    //Флаг, означающий, играет ли трэк в данный момент
    var isPlaying: Boolean

)