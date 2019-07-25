package com.strikalov.testitunesapp.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.strikalov.testitunesapp.R
import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.model.entity.Track
import com.strikalov.testitunesapp.view.adapters.RecyclerTrackAdapter
import com.strikalov.testitunesapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    companion object {

        private const val TAG = "DETAIL_ACTIVITY_TAG"

        private const val GET_ALBUM = "GET_ALBUM"

        fun newIntent(context: Context, album: Album): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(GET_ALBUM, album)
            return intent
        }

    }

    val detailViewModel: DetailViewModel by viewModel()

    private lateinit var recyclerTrackAdapter : RecyclerTrackAdapter

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        intent?.let {

            val album: Album = it.getParcelableExtra(GET_ALBUM)
            detailViewModel.onAlbumGet(album, getString(R.string.songs),isNetworkOnline())

            if(!isNetworkOnline()){
                progress_bar.visibility = ProgressBar.GONE
                showToast(R.string.no_network_connection)
            }
        }

        prepareToGetAlbumInformation()
        initRecyclerView()
        prepareRecyclerViewToUpdate()
        initMediaPlayer()

    }

    private fun initMediaPlayer(){
        detailViewModel.getNowPlayingTrack().observe(this, Observer {
            releaseMediaPlayer()
            if(it != null){
                if(it.trackUrl != null) {
                    try {
                        mediaPlayer = MediaPlayer()
                        mediaPlayer?.setDataSource(it.trackUrl)
                        mediaPlayer?.setOnPreparedListener(this)
                        mediaPlayer?.prepareAsync()
                        mediaPlayer?.setOnCompletionListener(this)
                    } catch (e: Exception) {
                        Log.i(TAG, e.toString())
                        showToast(R.string.music_playback_error)
                    }
                }else{
                    showToast(R.string.track_has_no_music_url)
                }
            }
        })

    }

    private fun releaseMediaPlayer(){
        if(mediaPlayer != null){
            try {
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (e: Exception) {
                Log.i(TAG, e.toString())
                showToast(R.string.music_playback_error)
            }

        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        detailViewModel.onTrackComplete()
    }

    private fun prepareToGetAlbumInformation(){

        detailViewModel.getAlbumName().observe(this, Observer {
            album_name.text = it
        })

        detailViewModel.getArtistName().observe(this, Observer {
            artist_name.text = it
        })

        detailViewModel.getGenreAndReleaseYear().observe(this, Observer {
            genre_and_release_year.text = it
        })

        detailViewModel.getAlbumPhotoUrl().observe(this, Observer {
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.placeholderimage)
                .into(album_photo)
        })

        detailViewModel.getAlbumDetail().observe(this, Observer {
            album_detail.text = it
        })

    }

    private fun initRecyclerView(){
        recyclerTrackAdapter = RecyclerTrackAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = recyclerTrackAdapter
        recyclerTrackAdapter.onItemClickListener = object: RecyclerTrackAdapter.OnItemClickListener{
            override fun onItemClicked(position: Int) {
                detailViewModel.onTrackClick(position)
            }

        }
    }

    private fun prepareRecyclerViewToUpdate(){
        detailViewModel.getTrackList().observe(this, Observer {
            progress_bar.visibility = ProgressBar.GONE
            it?.let {trackList ->
                updateRecyclerView(trackList)
            }
        })
    }

    private fun updateRecyclerView(listTrack: List<Track>){
        recyclerTrackAdapter.trackList = listTrack
        recyclerTrackAdapter.notifyDataSetChanged()
    }

    private fun isNetworkOnline(): Boolean{

        val connectivityManager: ConnectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return (networkInfo != null && networkInfo.isConnected)
    }

    private fun showToast(resId: Int){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
    }
}
