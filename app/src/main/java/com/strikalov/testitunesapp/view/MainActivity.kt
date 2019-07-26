package com.strikalov.testitunesapp.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.strikalov.testitunesapp.R
import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.view.adapters.RecyclerAlbumAdapter
import com.strikalov.testitunesapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * MainActivity представляет экран для поиска альбомов
 */
class MainActivity : AppCompatActivity() {

    //Инъекция MainViewModel с помощью библиотеки Koin
    val mainViewModel: MainViewModel by viewModel()

    //Адаптер для RecyclerView для отображения музыкальных альбомов
    private lateinit var recyclerAlbumAdapter : RecyclerAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Инициализируем RecyclerView
        initRecyclerView()

        //Подписываемся на albumListLiveData из MainViewModel и ожидаем список найденных альбомов
        mainViewModel.getAlbumList().observe(this, Observer { albumList ->

            //Прячем прогрессбар
            progress_bar.visibility = ProgressBar.GONE

            albumList?.let {
                if(it.isEmpty()){
                    //Если список пуст, показываем тост, что список пуст
                    showToast(R.string.nothing_found)
                }
                //Предоставляем список альбомов в RecyclerView и просим его обновиться
                updateRecyclerView(it)
            } ?: showToast(R.string.nothing_found)

        })

        //Вешаем слушатель на кнопку поиска
        main_search_button.setOnClickListener {
            //если произошел клик, вызываем метод onQueryEntered
            onQueryEntered(isNetworkOnline())
        }

    }

    //Инициализируем RecyclerView
    private fun initRecyclerView(){
        recyclerAlbumAdapter = RecyclerAlbumAdapter()
        recycler_view.layoutManager = GridLayoutManager(this,2)
        recycler_view.adapter = recyclerAlbumAdapter

        //Вешаем слушатель на каждый элемент RecyclerView, если произошел клик
        //получаем обьект альбома по которому кликнули, стартуем DetailActivity и передаем в нее
        //обьект этого альбома
        recyclerAlbumAdapter.onItemClickListener = object: RecyclerAlbumAdapter.OnItemClickListener{
            override fun onItemClicked(album: Album) {
                val intent = DetailActivity.newIntent(this@MainActivity, album)
                startActivity(intent)
            }

        }
    }

    //Передаем в RecyclerView новый список альбомов и просим обновиться на экране
    private fun updateRecyclerView(listAlbum: List<Album>){
        recyclerAlbumAdapter.albumList = listAlbum
        recyclerAlbumAdapter.notifyDataSetChanged()
    }

    //Метод вызывается, когда пользователь ввел запрос для поиска альбома и нажал на кнопку
    private fun onQueryEntered(isNetworkOnline: Boolean){

        //Получаем запрос из EditText и очищаем от лишних пробелов
        val query = main_edit_text.text.toString().trim()

        //Прячем клавиатуру
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            main_edit_text.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        if(!isNetworkOnline){

            //Если нет связи с интернетом, показываем соответсвующий тост
            showToast(R.string.no_network_connection)

        }else if(TextUtils.isEmpty(query)){

            //Если поисковый запрос пуст, показываем соответствующий тост
            showToast(R.string.search_request_is_empty)
            main_edit_text.setText("")

        }else{

            //Если с интернетом и запросов все нормально, показываем ProgressBar
            //и просим mainViewModel начать поиск и загрузку подходящих альбомов
            main_edit_text.setText("")
            progress_bar.visibility = ProgressBar.VISIBLE

            mainViewModel.onQueryEntered(query)

        }

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
}
