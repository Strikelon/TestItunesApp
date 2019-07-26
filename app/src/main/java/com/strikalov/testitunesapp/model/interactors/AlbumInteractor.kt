package com.strikalov.testitunesapp.model.interactors

import com.strikalov.testitunesapp.model.entity.Album
import io.reactivex.Observable

interface AlbumInteractor{

    /**
     * Метод возвращает отсортированный список альбомов обернутый в Observable от RxJava
     */
    fun downloadSortedAlbumList(term: String): Observable<List<Album>>

}