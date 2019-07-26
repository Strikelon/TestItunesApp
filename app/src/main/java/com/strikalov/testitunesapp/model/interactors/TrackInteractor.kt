package com.strikalov.testitunesapp.model.interactors

import com.strikalov.testitunesapp.model.entity.Track
import io.reactivex.Observable

interface TrackInteractor {

    /**
     * Метод возвращает список музыкальный трэков обернутый в Observable от RxJava
     */
    fun downloadTrackList(albumId: String): Observable<List<Track>>

}