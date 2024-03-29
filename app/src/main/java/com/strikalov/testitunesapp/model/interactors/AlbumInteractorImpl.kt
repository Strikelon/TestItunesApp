package com.strikalov.testitunesapp.model.interactors

import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.repositories.NetworkRepository
import io.reactivex.Observable
import java.util.*

class AlbumInteractorImpl(val networkRepository: NetworkRepository): AlbumInteractor {

    /**
     * Получаем от networkRepository списов альбомов, сортируем его и возвращаем отсортированный
     * список
     */
    override fun downloadSortedAlbumList(term: String): Observable<List<Album>> {

        return networkRepository.downloadAlbumList(term)
            .map {

                it.sort()

                return@map it.toList()
            }
    }
}