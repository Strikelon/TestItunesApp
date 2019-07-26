package com.strikalov.testitunesapp.model.repositories

import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.entity.Track
import io.reactivex.Observable

interface NetworkRepository {

    /**
     * Метод возвращает список музыкальных альбомов полученных от сервера и преобразованных
     * в сущности бизнес-логики
     */
    fun downloadAlbumList(term: String): Observable<MutableList<Album>>

    /**
     * Метод возвращает список музыкальных трэков полученных от сервера и преобразованных
     * в сущности бизнес-логики
     */
    fun downloadTrackList(albumId: String): Observable<MutableList<Track>>

}