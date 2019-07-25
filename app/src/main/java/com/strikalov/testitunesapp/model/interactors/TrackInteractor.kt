package com.strikalov.testitunesapp.model.interactors

import com.strikalov.testitunesapp.model.entity.Track
import io.reactivex.Observable

interface TrackInteractor {

    fun downloadTrackList(albumId: String): Observable<List<Track>>

}