package com.idonse.mymovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import com.idonse.mymovies.adapters.MovieAdapter
import com.idonse.mymovies.data.MainViewModel
import com.idonse.mymovies.data.Movie
import com.idonse.mymovies.utils.JSONUtils
import com.idonse.mymovies.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<JSONObject> {

    private lateinit var adapter: MovieAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var loaderManager: LoaderManager

    companion object{
        private val LOADER_ID = 31
        private var page = 1
        private var isLoading = false
        private var methodOfSort = NetworkUtils.POPULARITY
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

    private fun getColumnCount():Int{
        var displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = (displayMetrics.widthPixels/ displayMetrics.density).toInt()
        return if(width/185 > 2) (width / 185).toInt() else 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lang = Locale.getDefault().language
//        var jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.TOP_RATED, 3)
//        if(jsonObject== null)
//            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
//        else
//            Toast.makeText(this, "congratulate", Toast.LENGTH_SHORT).show()
//        Log.i("Result: ",jsonObject.toString())
        ///////////////////////////////////////////
//        var jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY, 5)
//        var movies = JSONUtils.getMoviesFromJSON(jsonObject)
//        var buider = StringBuilder()
//        for(movie in movies){
//            buider.append(movie.title).append("\n")
//        }
//        Log.i("Result", buider.toString())
        loaderManager = LoaderManager.getInstance(this)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        adapter = MovieAdapter()
        recyclerViewPosters.layoutManager = GridLayoutManager(this, getColumnCount())
        switchSort.isChecked = true
        recyclerViewPosters.adapter = adapter
        switchSort.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                methodOfSort = NetworkUtils.TOP_RATED
                textViewTopRated.setTextColor(resources.getColor(R.color.colorAccent))
                textViewPopularity.setTextColor(resources.getColor(R.color.white_color))
            } else {
                methodOfSort = NetworkUtils.POPULARITY
                textViewPopularity.setTextColor(resources.getColor(R.color.colorAccent))
                textViewTopRated.setTextColor(resources.getColor(R.color.white_color))
            }
            page = 1
            downloadData(methodOfSort, page)
        }
        switchSort.isChecked = false
        adapter.onPosterClickListener = object : MovieAdapter.OnPosterClickListener{
            override fun onPosterClick(position: Int) {
                //Toast.makeText(this@MainActivity, "Clicked:$position", Toast.LENGTH_SHORT).show()
                var movie = adapter.movies[position]
                var intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                startActivity(intent)
            }

        }
        adapter.onReachEndListener = object : MovieAdapter.OnReachEndListener{
            override fun onReachEnd() {
                if (!isLoading) {
                    //Toast.makeText(this@MainActivity, "End of List", Toast.LENGTH_SHORT).show()
                    downloadData(methodOfSort, page)
                }
            }
        }
        textViewPopularity.setOnClickListener {
            switchSort.isChecked = false
        }
        textViewTopRated.setOnClickListener {
            switchSort.isChecked = true
        }
        var moviesFromLiveData = viewModel.movies
        moviesFromLiveData.observe(this, Observer<List<Movie>> {
            //adapter.movies = it as MutableList<Movie>
            if(page == 1){
                adapter.movies = it as MutableList<Movie>
            }
        })

    }

    private fun downloadData(methodOfSort : Int, page: Int){
        //old release version
//        var jsonObject = NetworkUtils.getJSONFromNetwork(methodOfSort, 1)
//        var movies = JSONUtils.getMoviesFromJSON(jsonObject)
//        if(movies.isNotEmpty()){
//            viewModel.deleteAllMovies()
//            for(movie in movies){
//                viewModel.insertMovie(movie)
//            }
//        }
//        adapter.movies = movies
        var url = NetworkUtils.buildURL(methodOfSort,page, lang)
        var bundle = Bundle()
        bundle.putString("url",url.toString())
        loaderManager.restartLoader(LOADER_ID, bundle, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<JSONObject> {
        var jsonLoader = NetworkUtils.Companion.JSONLoader(this, args)
        jsonLoader.onStartLoadingListener = object: NetworkUtils.Companion.JSONLoader.OnStartLoadingListener{
            override fun onStartLoading() {
                progressBarLoading.visibility = View.VISIBLE
                isLoading = true
            }
        }
        return jsonLoader
    }

    override fun onLoadFinished(loader: Loader<JSONObject>, data: JSONObject?) {
        var movies = JSONUtils.getMoviesFromJSON(data)
        if(movies.isNotEmpty()){
            if(page == 1) {
                viewModel.deleteAllMovies()
                adapter.clear()
            }
            for(movie in movies){
                viewModel.insertMovie(movie)
            }
            adapter.addMovies(movies)
            page++
        }
        isLoading = false
        progressBarLoading.visibility = View.INVISIBLE
        loaderManager.destroyLoader(LOADER_ID)
    }

    override fun onLoaderReset(loader: Loader<JSONObject>) {

    }

}
