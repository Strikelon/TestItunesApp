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
        //Константа для логирования
        private const val TAG = "DETAIL_VIEW_MODEL"
    }

    private var disposable: Disposable? = null

    //LiveData содержащая имя альбома
    private val albumNameLiveData = MutableLiveData<String>()

    //LiveData содержащая имя артиста или музыкального коллектива
    private val artistNameLiveData = MutableLiveData<String>()

    //LiveData содержащая музыкальный жанр альбома и год релиза
    private val genreAndReleaseYearLiveData = MutableLiveData<String>()

    //LiveData содержащая ссылка на картинку альбома
    private val albumPhotoUrlLiveData = MutableLiveData<String>()

    //LiveData содержащая список трэков и стоимость альбома
    private val albumDetailLiveData = MutableLiveData<String>()

    //LiveData содержащая список полученных от сервера музыкальных трэков
    private val trackListLiveData = MutableLiveData<List<Track>>()

    //LiveData содержащая играющий сейчас трэк
    private val nowPlayingTrackLiveData = MutableLiveData<Track>()

    //Номер позиции играющего сейчас трэка
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

    /**
     * Метод срабатывает, при передачи из DetailActivity обьекта класса трэк,
     * выбранного пользователм
     */
    fun onAlbumGet(album: Album, songsString: String, isNetworkOnline: Boolean){

        // передаем имя альбома в albumNameLiveData.value
        albumNameLiveData.value = album.albumName

        // передаем имя артиста в artistNameLiveData
        artistNameLiveData.value = album.artistName ?: ""

        // передаем жанр и год релиза альбома в genreAndReleaseYearLiveData
        genreAndReleaseYearLiveData.value = "${album.primaryGenreName ?: ""} - ${album.releaseYear ?: ""}"

        // передаем ссылку на картинку альбома в albumPhotoUrlLiveData
        albumPhotoUrlLiveData.value = album.pictureUrl

        // передаем количество трэков и стоимость альбом с указанием валюты в albumDetailLiveData
        albumDetailLiveData.value = "${album.trackCount ?: ""} $songsString - ${album.albumPrice ?: ""} ${album.currency ?: ""}"

        // если связь с интернет есть, пробуем загрузиь список трэков по id альбома
        if(isNetworkOnline) {
            downloadTrackList(album.albumId)
        }
    }

    /**
     * Метод загружает список трэков с сервера по id альбома
     */
    private fun downloadTrackList(albumId: String){

        disposable = trackInteractor.downloadTrackList(albumId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    //если список успешно загрузился, передаем значение
                    //в trackListLiveData, чтобы она оповестила DetailActivity
                    trackListLiveData.value = it
                },
                {
                    Log.i(TAG, it.toString())
                }
            )

    }

    /**
     * Метод вызывается, когда пользователь нажал на трэк.
     * В данном методе мы меня флаг isPlaying трэков на false или true,
     * а зависимости от того, играет он, или его выключили
     */
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

    /**
     * Метод вызывается, когда проигрывания трэка завершается,
     * в данном методе мы меняем флаг isPlaying, трэка, который проигрывался на false
     */
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

    /**
     * При уничтожении DetailLiveData освобождаемся от связи с RxJava
     */
    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}