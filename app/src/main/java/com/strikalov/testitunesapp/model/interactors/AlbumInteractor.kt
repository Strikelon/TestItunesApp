package com.strikalov.testitunesapp.model.interactors

import com.strikalov.testitunesapp.model.entity.Album
import io.reactivex.Observable

interface AlbumInteractor{

    fun downloadSortedAlbumList(term: String): Observable<List<Album>>

}