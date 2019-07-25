package com.strikalov.testitunesapp.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.strikalov.testitunesapp.R
import com.strikalov.testitunesapp.model.entity.Album
import kotlinx.android.synthetic.main.recycler_album_item.view.*

class RecyclerAlbumAdapter(var albumList: List<Album> = ArrayList()) : RecyclerView.Adapter<RecyclerAlbumAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClicked(album: Album)
    }

    var onItemClickListener: OnItemClickListener? = null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it.onItemClicked(albumList[adapterPosition])
                }
            }

        }

        fun bindItem(album: Album){

            Picasso.get()
                .load(album.pictureUrl)
                .placeholder(R.drawable.placeholderimage)
                .into(itemView.album_image)

            itemView.album_name.text = album.albumName
        }

    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAlbumAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_album_item,
            parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAlbumAdapter.ViewHolder, position: Int) {
        holder.bindItem(albumList[position])
    }

}