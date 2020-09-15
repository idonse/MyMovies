package com.idonse.mymovies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idonse.mymovies.R
import com.idonse.mymovies.data.Review

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    var reviews: ArrayList<Review> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textViewAuthor = itemView.findViewById<TextView>(R.id.textViewAuthor)
        internal var textViewContent = itemView.findViewById<TextView>(R.id.textViewContent)

        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        var review = reviews[position]
        holder.textViewContent.text = review.content
        holder.textViewAuthor.text = review.author
    }
}