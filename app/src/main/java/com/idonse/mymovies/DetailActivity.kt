package com.idonse.mymovies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.idonse.mymovies.adapters.ReviewAdapter
import com.idonse.mymovies.adapters.TrailerAdapter
import com.idonse.mymovies.data.FavouriteMovie
import com.idonse.mymovies.data.MainViewModel
import com.idonse.mymovies.data.Movie
import com.idonse.mymovies.utils.JSONUtils
import com.idonse.mymovies.utils.NetworkUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.movie_info.*
import org.json.JSONObject
import java.util.*

class DetailActivity : AppCompatActivity() {
    private var id = 0
    private lateinit var viewModel: MainViewModel
    private  var movie: Movie? = null
    private  var favouriteMovie: FavouriteMovie? = null
    private var reviewAapter: ReviewAdapter = ReviewAdapter()
    private  var trailerAdapter: TrailerAdapter = TrailerAdapter()

    companion object{
        private lateinit var lang:String
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        when(id){
            R.id.itemMain -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.itemFavourite -> {
                var intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        lang = Locale.getDefault().language
        id = intent.getIntExtra("id", -1)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        movie = viewModel.getMovieById(id)
        Picasso.get().load(movie?.bigPosterPath).placeholder(R.drawable.placeholder_large).into(imageViewBigPoster)
        textViewTitle.text = movie?.title
        textViewOriginalTitle.text = movie?.originalTitle
        textViewOverview.text = movie?.overview
        textViewReleaseDate.text = movie?.releaseDate
        textViewRating.text = movie?.voteAverage.toString()
        setFavourite()
        imageViewAddToFavorite.setOnClickListener {
            if (favouriteMovie == null){
                viewModel.insertFavouriteMovie(FavouriteMovie(movie as Movie))
                Toast.makeText(this@DetailActivity, getString(R.string.add_to_favourite), Toast.LENGTH_SHORT).show()
            } else{
                viewModel.deleteFavouriteMovie(FavouriteMovie(movie as Movie))
                Toast.makeText(this@DetailActivity, getString(R.string.remove_from_favourite), Toast.LENGTH_SHORT).show()
            }
            setFavourite()
        }

        trailerAdapter.onTrailerClickListener = object : TrailerAdapter.OnTrailerClickListener{
            override fun onTrailerClick(url: String) {
                //Toast.makeText(this@DetailActivity, url, Toast.LENGTH_SHORT).show()
                var intentToTrailer = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intentToTrailer)
            }

        }

        recyclerViewReviews.layoutManager = LinearLayoutManager(this)
        recyclerViewTrailers.layoutManager = LinearLayoutManager(this)
        recyclerViewReviews.adapter = reviewAapter
        recyclerViewTrailers.adapter = trailerAdapter
        var jsonObjectTrailers = NetworkUtils.getJSONForVideos(id, lang)
        var jsonObjectReciews = NetworkUtils.getJSONForReviews(id, lang)
        var trailers = JSONUtils.getTrailersFromJSON(jsonObjectTrailers)
        var reviews = JSONUtils.getReviewsFromJSON(jsonObjectReciews)
        reviewAapter.reviews = reviews
        trailerAdapter.trailers = trailers
        scrollViewInfo.smoothScrollTo(0, 0)

    }

    private fun setFavourite(){
        favouriteMovie = viewModel.getFavouriteMovieById(id)
        if (favouriteMovie == null){
            imageViewAddToFavorite.setImageResource(R.drawable.favourite_add_to)
        } else {
            imageViewAddToFavorite.setImageResource(R.drawable.favourite_remove)
        }
    }
}
