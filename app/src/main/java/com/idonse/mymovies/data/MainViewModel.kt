package com.idonse.mymovies.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        private lateinit var database: MovieDatabase
    }

    var movies: LiveData<List<Movie>>
    var favouriteMovies: LiveData<List<FavouriteMovie>>

    init {
        database = MovieDatabase.getInstance(application)
        movies = database.movieDao().getAllMovies()
        favouriteMovies = database.movieDao().getAllFavouriteMovies()
    }

    fun getMovieById(id:Int): Movie? {
        try {
            return GetMovieTask().execute(id).get()
        } catch (e: Exception) {
        }
        return null
    }

    private inner class GetMovieTask: AsyncTask<Int, Unit, Movie?>() {
        override fun doInBackground(vararg p0: Int?): Movie? {
            if (p0.isNotEmpty()){
                return database.movieDao().getMovieById(p0[0] as Int)
            }
            return null
        }
    }

    fun deleteAllMovies(){
        DeleteMoviesTask().execute()
    }

    private inner class DeleteMoviesTask: AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit?) {
            database.movieDao().deleteAllMovies()
        }

    }

    fun insertFavouriteMovie(movie: FavouriteMovie){
        InsertFavouriteTask().execute(movie)
    }

    private inner class InsertFavouriteTask: AsyncTask<FavouriteMovie, Unit, Unit>() {
        override fun doInBackground(vararg p0: FavouriteMovie?) {
            if (p0.isNotEmpty()){
                database.movieDao().insertFavouriteMovie(p0[0] as FavouriteMovie)
            }
        }
    }

    fun deleteFavouriteMovie(movie: FavouriteMovie){
        DeleteFavouriteTask().execute(movie)
    }

    private inner class DeleteFavouriteTask: AsyncTask<FavouriteMovie, Unit, Unit>() {
        override fun doInBackground(vararg p0: FavouriteMovie?) {
            if (p0.isNotEmpty()){
                database.movieDao().deleteFavouriteMovie(p0[0] as FavouriteMovie)
            }
        }
    }

    fun insertMovie(movie: Movie){
        InsertTask().execute(movie)
    }

    private inner class InsertTask: AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg p0: Movie?) {
            if (p0.isNotEmpty()){
                database.movieDao().insertMovie(p0[0] as Movie)
            }
        }
    }

    fun deleteMovie(movie: Movie){
        DeleteTask().execute(movie)
    }

    private inner class DeleteTask: AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg p0: Movie?) {
            if (p0.isNotEmpty()){
                database.movieDao().deleteMovie(p0[0] as Movie)
            }
        }
    }

    fun getFavouriteMovieById(id:Int): FavouriteMovie? {
        try {
            return GetFavouriteMovieTask().execute(id).get()
        } catch (e: Exception) {
        }
        return null
    }

    private inner class GetFavouriteMovieTask: AsyncTask<Int, Unit, FavouriteMovie?>() {
        override fun doInBackground(vararg p0: Int?): FavouriteMovie? {
            if (p0.isNotEmpty()){
                return database.movieDao().getFavouriteMovieById(p0[0] as Int)
            }
            return null
        }
    }



}