package com.strikalov.testitunesapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.util.Log
import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.interactors.AlbumInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class MainViewModel(val albumInteractor: AlbumInteractor) : ViewModel(){

    companion object {
        //Константа для логирования
        private const val TAG = "MAIN_VIEW_MODEL"
    }

    private var disposable: Disposable? = null

    //albumListLiveData - содержит список найденных альбомов
    private val albumListLiveData = MutableLiveData<List<Album>>()

    fun getAlbumList(): LiveData<List<Album>>{
        return albumListLiveData
    }

    /**
     * Метод вызывается, когда пользователь ввел свой запрос для поиска альбома
     * и нажал кнопку найти
     */
    fun onQueryEntered(query: String){

        //Запрашиваем с помощью RxJava у albumInteractor список найденных и отсортированных
        //альбомов
        disposable = albumInteractor.downloadSortedAlbumList(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    //в случаем успеха, передаем список в albumListLiveData,
                    //чтобы он оповестил MainActivity о найденном спике и отдал его
                    albumListLiveData.value = it
                },
                {
                    albumListLiveData.value = ArrayList()
                    Log.i(TAG, it.toString())
                }
            )

    }


    override fun onCleared() {
        super.onCleared()
        //При уничтожении ViewModel отписываемся от Observable RxJava
        disposable?.dispose()
    }
}