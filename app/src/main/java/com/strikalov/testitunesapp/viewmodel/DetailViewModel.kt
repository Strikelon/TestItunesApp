package com.strikalov.testitunesapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.strikalov.testitunesapp.R
import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.entity.Track
import com.strikalov.testitunesapp.model.interactors.TrackInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class DetailViewModel(val trackInteractor: TrackInteractor): ViewModel() {

    companion object {
        private const val TAG = "DETAIL_VIEW_MODEL"
    }

    private var disposable: Disposable? = null

    private val albumNameLiveData = MutableLiveData<String>()

    private val artistNameLiveData = MutableLiveData<String>()

    private val genreAndReleaseYearLiveData = MutableLiveData<String>()

    private val albumPhotoUrlLiveData = MutableLiveData<String>()

    private val albumDetailLiveData = MutableLiveData<String>()

    private val trackListLiveData = MutableLiveData<List<Track>>()

    private val nowPlayingTrackLiveData = MutableLiveData<Track>()

    private var nowPlayingTrackPosition : Int? = null

    fun getAlbumName(): LiveData<String>{
        return albumNameLiveData
    }

    fun getArtistName(): LiveData<String>{
        return artistNameLiveData
    }

    fun getGenreAndReleaseYear(): LiveData<String>{
        return genreAndReleaseYearLiveData
    }

    fun getAlbumPhotoUrl(): LiveData<String>{
        return albumPhotoUrlLiveData
    }

    fun getAlbumDetail(): LiveData<String>{
        return albumDetailLiveData
    }

    fun getTrackList(): LiveData<List<Track>>{
        return trackListLiveData
    }

    fun getNowPlayingTrack(): LiveData<Track>{
        return nowPlayingTrackLiveData
    }

    fun onAlbumGet(album: Album, songsString: String, isNetworkOnline: Boolean){

        albumNameLiveData.value = album.albumName

        artistNameLiveData.value = album.artistName ?: ""

        genreAndReleaseYearLiveData.value = "${album.primaryGenreName ?: ""} - ${album.releaseYear ?: ""}"

        albumPhotoUrlLiveData.value = album.pictureUrl

        albumDetailLiveData.value = "${album.trackCount ?: ""} $songsString - ${album.albumPrice ?: ""} ${album.currency ?: ""}"

        if(isNetworkOnline) {
            downloadTrackList(album.albumId)
        }
    }

    private fun downloadTrackList(albumId: String){

        disposable = trackInteractor.downloadTrackList(albumId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    trackListLiveData.value = it
                },
                {
                    Log.i(TAG, it.toString())
                }
            )

    }

    fun onTrackClick(newPosition: Int){

        if( trackListLiveData.value!= null) {
            val trackList: MutableList<Track> = ArrayList(trackListLiveData.value)

            if (nowPlayingTrackPosition == null) {

                nowPlayingTrackPosition = newPosition
                trackList[nowPlayingTrackPosition!!].isPlaying = true
                nowPlayingTrackLiveData.value = trackList[nowPlayingTrackPosition!!]
                trackListLiveData.value = trackList.toList()

            }else if (nowPlayingTrackPosition == newPosition){

                trackList[nowPlayingTrackPosition!!].isPlaying = false
                nowPlayingTrackPosition = null
                nowPlayingTrackLiveData.value = null
                trackListLiveData.value = trackList.toList()

            }else if(nowPlayingTrackPosition != null && nowPlayingTrackPosition != newPosition){
                trackList[nowPlayingTrackPosition!!].isPlaying = false
                trackList[newPosition].isPlaying = true
                nowPlayingTrackLiveData.value = trackList[newPosition]
                nowPlayingTrackPosition = newPosition
                trackListLiveData.value = trackList.toList()
            }
        }
    }

    fun onTrackComplete(){

        if( trackListLiveData.value!= null) {

            val trackList: MutableList<Track> = ArrayList(trackListLiveData.value)

            if (nowPlayingTrackPosition != null) {

                trackList[nowPlayingTrackPosition!!].isPlaying = false
                nowPlayingTrackPosition = null
                nowPlayingTrackLiveData.value = null
                trackListLiveData.value = trackList.toList()

            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}