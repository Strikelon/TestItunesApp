package com.strikalov.testitunesapp.model.interactors

import com.strikalov.testitunesapp.model.entity.Track
import com.strikalov.testitunesapp.model.repositories.NetworkRepository
import io.reactivex.Observable

class TrackInteractorImpl(val networkRepository: NetworkRepository): TrackInteractor {

    /**
     * Метод возвращает список музыкальный трэков обернутый в Observable от RxJava
     */
    override fun downloadTrackList(albumId: String): Observable<List<Track>> {
        return networkRepository.downloadTrackList(albumId)
            .map {
                return@map it.toList()
            }
    }
}