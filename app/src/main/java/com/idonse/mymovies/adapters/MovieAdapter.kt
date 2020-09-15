package com.idonse.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.idonse.mymovies.R
import com.idonse.mymovies.data.Movie
import com.squareup.picasso.Picasso

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var movies: MutableList<Movie> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onPosterClickListener: OnPosterClickListener? = null
    var onReachEndListener: OnReachEndListener? = null

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var imageViewSmallPoster = itemView.findViewById<ImageView>(R.id.imageViewSmallPoster)
        init{
            itemView.setOnClickListener {
                onPosterClickListener?.onPosterClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(movies.size >= 20 && position > movies.size -4) {
            onReachEndListener?.onReachEnd()
        }
        var movie = movies[position]
        Picasso.get().load(movie.posterPath).into(holder.imageViewSmallPoster)
    }

    fun addMovies(movies: List<Movie>){
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun clear(){
        this.movies.clear()
        notifyDataSetChanged()
    }

    interface OnPosterClickListener{
        fun onPosterClick(position:Int)
    }

    interface OnReachEndListener{
        fun onReachEnd()
    }
}