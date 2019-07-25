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
        private const val TAG = "MAIN_VIEW_MODEL"
    }

    private var disposable: Disposable? = null

    private val albumListLiveData = MutableLiveData<List<Album>>()

    fun getAlbumList(): LiveData<List<Album>>{
        return albumListLiveData
    }

    fun onQueryEntered(query: String){

        disposable = albumInteractor.downloadSortedAlbumList(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
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
        disposable?.dispose()
    }
}