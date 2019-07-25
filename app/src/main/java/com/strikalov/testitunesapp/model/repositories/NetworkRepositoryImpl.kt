package com.strikalov.testitunesapp.model.repositories

import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.entity.Track
import com.strikalov.testitunesapp.model.network.NetworkApi
import com.strikalov.testitunesapp.model.network.networkentity.CollectionJson
import com.strikalov.testitunesapp.model.network.networkentity.TrackJson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class NetworkRepositoryImpl(val networkApi: NetworkApi): NetworkRepository {

    override fun downloadAlbumList(term: String): Observable<MutableList<Album>> {

        return networkApi.downloadCollectionResult(term)
            .map {

                val albumList: MutableList<Album> = ArrayList()

                for(collectionJson in it.results){

                    albumList.add(createAlbum(collectionJson))

                }

                return@map albumList

            }.subscribeOn(Schedulers.io())
    }

    override fun downloadTrackList(albumId: String): Observable<MutableList<Track>> {
        return networkApi.downloadTrackResult(albumId)
            .map {

                val trackList: MutableList<Track> = ArrayList()

                for(trackJson in it.results){

                    if(trackJson.wrapperType == NetworkApi.TRACK_WRAPPER){
                        trackList.add(createTrack(trackJson))
                    }

                }

                return@map trackList

            }.subscribeOn(Schedulers.io())
    }

    private fun createAlbum(collectionJson: CollectionJson): Album{

        val cuttingIndex: Int = collectionJson.releaseDate.indexOf('-')

        val releaseYear: String =
            if(cuttingIndex != -1) collectionJson.releaseDate.substring(0, cuttingIndex)
            else collectionJson.releaseDate

        return Album(
            albumId = collectionJson.collectionId,
            albumName = collectionJson.collectionName,
            artistName = collectionJson.artistName,
            releaseYear = releaseYear,
            pictureUrl = collectionJson.artworkUrl100,
            primaryGenreName = collectionJson.primaryGenreName,
            trackCount = collectionJson.trackCount,
            albumPrice = collectionJson.collectionPrice,
            currency  = collectionJson.currency
        )

    }

    private fun createTrack(trackJson: TrackJson): Track{
        return Track(
            trackName = trackJson.trackName,
            trackUrl = trackJson.previewUrl,
            trackPrice = trackJson.trackPrice,
            currency = trackJson.currency,
            isPlaying = false
        )
    }
}