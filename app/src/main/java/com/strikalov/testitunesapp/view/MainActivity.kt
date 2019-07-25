package com.strikalov.testitunesapp.view

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.strikalov.testitunesapp.R
import com.strikalov.testitunesapp.model.entity.Album
import com.strikalov.testitunesapp.view.adapters.RecyclerAlbumAdapter
import com.strikalov.testitunesapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel




class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MAIN_ACTIVITY_TAG"
    }

    val mainViewModel: MainViewModel by viewModel()

    private lateinit var recyclerAlbumAdapter : RecyclerAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()

        mainViewModel.getAlbumList().observe(this, Observer { albumList ->

            progress_bar.visibility = ProgressBar.GONE

            albumList?.let {
                if(it.isEmpty()){
                    showToast(R.string.nothing_found)
                }
                updateRecyclerView(it)
            } ?: showToast(R.string.nothing_found)

        })

        main_search_button.setOnClickListener {
            onQueryEntered(isNetworkOnline())
        }

    }

    private fun initRecyclerView(){
        recyclerAlbumAdapter = RecyclerAlbumAdapter()
        recycler_view.layoutManager = GridLayoutManager(this,2)
        recycler_view.adapter = recyclerAlbumAdapter
        recyclerAlbumAdapter.onItemClickListener = object: RecyclerAlbumAdapter.OnItemClickListener{
            override fun onItemClicked(album: Album) {
                val intent = DetailActivity.newIntent(this@MainActivity, album)
                startActivity(intent)
            }

        }
    }

    private fun updateRecyclerView(listAlbum: List<Album>){
        recyclerAlbumAdapter.albumList = listAlbum
        recyclerAlbumAdapter.notifyDataSetChanged()
    }

    private fun onQueryEntered(isNetworkOnline: Boolean){

        val query = main_edit_text.text.toString().trim()

        //Прячем клавиатуру
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            main_edit_text.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        if(!isNetworkOnline){

            showToast(R.string.no_network_connection)

        }else if(TextUtils.isEmpty(query)){

            showToast(R.string.search_request_is_empty)
            main_edit_text.setText("")

        }else{

            main_edit_text.setText("")
            progress_bar.visibility = ProgressBar.VISIBLE

            mainViewModel.onQueryEntered(query)

        }

    }

    private fun isNetworkOnline(): Boolean{

        val connectivityManager: ConnectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return (networkInfo != null && networkInfo.isConnected)
    }

    private fun showToast(resId: Int){
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}
