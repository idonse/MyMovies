package com.idonse.mymovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.idonse.mymovies.adapters.MovieAdapter
import com.idonse.mymovies.data.FavouriteMovie
import com.idonse.mymovies.data.MainViewModel
import com.idonse.mymovies.data.Movie
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MainViewModel

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
        setContentView(R.layout.activity_favourite)
        recyclerViewFavouriteMovies.layoutManager = GridLayoutManager(this, 2)
        adapter = MovieAdapter()
        recyclerViewFavouriteMovies.adapter = adapter
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        var favouriteMovies = viewModel.favouriteMovies
        favouriteMovies.observe(this, Observer<List<FavouriteMovie>>{
            var movies : MutableList<Movie> = arrayListOf()
            movies.addAll(it)
            adapter.movies = movies
        })
        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener{
            override fun onPosterClick(position: Int) {
                //Toast.makeText(this@MainActivity, "Clicked:$position", Toast.LENGTH_SHORT).show()
                var movie = adapter.movies[position]
                var intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                startActivity(intent)
            }

        }
    }
}
