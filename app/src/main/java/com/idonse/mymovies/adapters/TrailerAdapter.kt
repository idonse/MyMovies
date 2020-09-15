package com.idonse.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idonse.mymovies.R
import com.idonse.mymovies.data.Trailer

class TrailerAdapter: RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    var trailers: ArrayList<Trailer> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onTrailerClickListener: OnTrailerClickListener? = null

    interface OnTrailerClickListener{
        fun onTrailerClick(url:String)
    }

    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        internal var textViewNameOfVideo = itemView.findViewById<TextView>(R.id.textViewNameOfVideo)
        init {
            itemView.setOnClickListener {
                onTrailerClickListener?.onTrailerClick(trailers[adapterPosition].key)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.trailer_item, parent, false)
        return TrailerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        var trailer = trailers[position]
        holder.textViewNameOfVideo.text = trailer.name
    }

}