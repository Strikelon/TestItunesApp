package com.strikalov.testitunesapp.model.repositories

import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.entity.Track
import io.reactivex.Observable

interface NetworkRepository {

    fun downloadAlbumList(term: String): Observable<MutableList<Album>>

    fun downloadTrackList(albumId: String): Observable<MutableList<Track>>

}