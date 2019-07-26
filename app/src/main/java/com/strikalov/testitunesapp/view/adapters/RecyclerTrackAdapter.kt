package com.strikalov.testitunesapp.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.strikalov.testitunesapp.R
import com.strikalov.testitunesapp.model.entity.Track
import kotlinx.android.synthetic.main.recycler_track_item.view.*

/**
 * Адаптер для RecyclerView, преобразует список обьектов класса Track
 * в элементы списка RecyclerView по макету R.layout.recycler_track_item
 */
class RecyclerTrackAdapter(var trackList: List<Track> = ArrayList())
    : RecyclerView.Adapter<RecyclerTrackAdapter.ViewHolder>() {


    interface OnItemClickListener{
        fun onItemClicked(position: Int)
    }

    //Слушатель, чтобы реагировать на клики по элементам RecyclerView представляющим музыкальный трэк
    var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it.onItemClicked(adapterPosition)
                }
            }

        }

        fun bindItem(track: Track){

            /**
             * Если песня сейчас проигрывается, показываем рядом картинку stop,
             * если песня не проигрывается показываем рядом картинку play
             */
            if(track.isPlaying){
                itemView.image_track_status.setImageResource(R.drawable.ic_action_stop)
            }else{
                itemView.image_track_status.setImageResource(R.drawable.ic_action_play)
            }

            val trackDetail = "${track.trackName ?: "Без названия"} - ${track.trackPrice ?: ""} ${track.currency ?: ""}"
            itemView.track_detail.text = trackDetail

        }

    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerTrackAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_track_item,
            parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerTrackAdapter.ViewHolder, position: Int) {
        holder.bindItem(trackList[position])
    }

}