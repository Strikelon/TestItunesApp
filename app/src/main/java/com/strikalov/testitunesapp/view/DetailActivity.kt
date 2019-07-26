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

/**
 * DetailActivity представляет экран с подробной информацией об альбоме
 * и списком трэков для прослушивания
 */
class DetailActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    companion object {

        //Константа для логирования
        private const val TAG = "DETAIL_ACTIVITY_TAG"

        //Константа для получения из intent полученного из MainActivity
        //обьекта альбома, выбранного пользователем
        private const val GET_ALBUM = "GET_ALBUM"

        //Статистичекий метод, для того, чтобы передать в intent обьект альбома, выбранного пользователем
        fun newIntent(context: Context, album: Album): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(GET_ALBUM, album)
            return intent
        }

    }

    //Инъекция DetailViewModel с помощью библиотеки Koin
    val detailViewModel: DetailViewModel by viewModel()

    //Адаптер для RecyclerView для отображения музыкальных трэков
    private lateinit var recyclerTrackAdapter : RecyclerTrackAdapter

    //МедиаПлеер для проигрывания музыкальных трэков
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        intent?.let {

            //Если intent не равен нулю, получаем обьект выбранного пользователем альбома
            //и отправляем в detailViewModel, с информацией есть ли связь с интернетом
            val album: Album = it.getParcelableExtra(GET_ALBUM)
            detailViewModel.onAlbumGet(album, getString(R.string.songs),isNetworkOnline())

            //Если связи с интернетом нет, прячем прогресс бар и показываем тост
            //с сообщением об этом
            if(!isNetworkOnline()){
                progress_bar.visibility = ProgressBar.GONE
                showToast(R.string.no_network_connection)
            }
        }

        //Подписываемся на несколько LiveData из DetailViewModel, чтобы
        //отобразить подробную информацию об альбоме
        prepareToGetAlbumInformation()

        //Инициализируем RecyclerView и вешаем слушатель кликов по музыкальным трэкам
        initRecyclerView()

        //Подписываемся на trackListLiveData из DetailViewModel,
        //чтобы получить список музыкальных трэков
        prepareRecyclerViewToUpdate()

        //Инициализируем MediaPlayer для прослушивания музыкальных трэков
        //и подписываемся на nowPlayingTrackLiveData из DetailViewModel, чтобы понимать, какой трэк проигрывать
        initMediaPlayer()

    }

    /**
     * Инициализируем MediaPlayer для прослушивания музыкальных трэков и
     * подписываемся на nowPlayingTrackLiveData из DetailViewModel, чтобы понимать, какой трэк проигрывать
     */
    private fun initMediaPlayer(){
        detailViewModel.getNowPlayingTrack().observe(this, Observer {
            //перезагрузка mediaPlayer
            releaseMediaPlayer()
            //Если полученный трэк не равен null
            if(it != null){
                //Если ссылка для проигрывания не равна null
                if(it.trackUrl != null) {
                    try {
                        //Инициализируем mediaPlayer и передаем ссылку для проигрывания
                        mediaPlayer = MediaPlayer()
                        mediaPlayer?.setDataSource(it.trackUrl)
                        mediaPlayer?.setOnPreparedListener(this)
                        mediaPlayer?.prepareAsync()
                        mediaPlayer?.setOnCompletionListener(this)
                    } catch (e: Exception) {
                        Log.i(TAG, e.toString())
                        //В случаем ошибки выводим тост
                        showToast(R.string.music_playback_error)
                    }
                }else{
                    //Если ссылка для проигрывания равна null выводим тост
                    showToast(R.string.track_has_no_music_url)
                }
            }
        })

    }

    /**
     * Метод перезагружает mediaPlayer
     */
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

    /**
     * Когда плеер загрузил ссылку и готов в воспроизведению, стартуем его
     */
    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
    }

    /**
     * Когда песня закончилась, сообщаем об этом detailViewModel,
     * чтобы он сменил трэку статус, на не проигрывается
     */
    override fun onCompletion(mp: MediaPlayer?) {
        detailViewModel.onTrackComplete()
    }

    /**
     * Подписываемся на несколько LiveData из DetailViewModel, чтобы
     * отобразить подробную информацию об альбоме
     */
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

    /**
     * Инициализируем RecyclerView и вешаем слушатель кликов по музыкальным трэкам
     */
    private fun initRecyclerView(){
        recyclerTrackAdapter = RecyclerTrackAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = recyclerTrackAdapter

        //Вешаем слушатель кликов на каждый музыкальный трэк, в слуае клика
        //передаем в detailViewModel позицию выбранного трэка
        recyclerTrackAdapter.onItemClickListener = object: RecyclerTrackAdapter.OnItemClickListener{
            override fun onItemClicked(position: Int) {
                detailViewModel.onTrackClick(position)
            }

        }
    }

    /**
     * Подписываемся на trackListLiveData из DetailViewModel,
     * чтобы получить список музыкальных трэков
     */
    private fun prepareRecyclerViewToUpdate(){
        detailViewModel.getTrackList().observe(this, Observer {
            // при получении спика трэков, прячем progressbar
            progress_bar.visibility = ProgressBar.GONE
            it?.let {trackList ->
                //в слуае если список не равен null
                //просим recyclerView обновиться
                updateRecyclerView(trackList)
            } ?: showToast(R.string.no_music_tracks)
        })
    }

    /**
     * Передаем в RecyclerView новый список музыкальных трэков и просим обновиться на экране
     */
    private fun updateRecyclerView(listTrack: List<Track>){
        recyclerTrackAdapter.trackList = listTrack
        recyclerTrackAdapter.notifyDataSetChanged()
    }

    //Метод проверяет есть ли связь с сетью и возвращает соответствующее булевое значение
    private fun isNetworkOnline(): Boolean{

        val connectivityManager: ConnectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return (networkInfo != null && networkInfo.isConnected)
    }

    //Метод показывает пользователю нужные тосты
    private fun showToast(resId: Int){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    //При уничтожении DetailActivity, перезагружаем и освобождаем MediaPlayer
    override fun onDestroy() {
        super.onDestroy()
        releaseMediaPlayer()
    }
}
