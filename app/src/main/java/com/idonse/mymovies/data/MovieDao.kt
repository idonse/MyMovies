package com.idonse.mymovies.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM favourite_movies")
    fun getAllFavouriteMovies(): LiveData<List<FavouriteMovie>>

    @Query("SELECT * FROM movies WHERE id == :movieId")
    fun getMovieById(movieId:Int): Movie

    @Query("SELECT * FROM favourite_movies WHERE id == :movieId")
    fun getFavouriteMovieById(movieId:Int): FavouriteMovie

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Insert
    fun insertMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Insert
    fun insertFavouriteMovie(movie: FavouriteMovie)

    @Delete
    fun deleteFavouriteMovie(movie: FavouriteMovie)
}