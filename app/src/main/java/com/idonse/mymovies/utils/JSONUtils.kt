package com.idonse.mymovies.utils

import com.idonse.mymovies.data.Movie
import com.idonse.mymovies.data.Review
import com.idonse.mymovies.data.Trailer
import org.json.JSONArray
import org.json.JSONObject

class JSONUtils {

    companion object{
        private val KEY_RESULTS = "results"

        //for reviews
        private val KEY_AUTHOR = "author"
        private val KEY_CONTENT = "content"

        //for trailers
        private val KEY_OF_VIDEO = "key"
        private val KEY_NAME = "name"
        private val BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v="

        //for full info about film
        private val KEY_VOTE_COUNT = "vote_count"
        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_ORIGINAL_TITLE = "original_title"
        private val KEY_OVERVIEW = "overview"
        private val KEY_POSTER_PATH = "poster_path"
        private val KEY_BACKDROP_PATH = "backdrop_path"
        private val KEY_RELEASE_DATE = "release_date"
        private val KEY_VOTE_AVERAGE= "vote_average"


        val BASE_POSTER_URL = "https://image.tmdb.org/t/p/"
        val SMALL_POSTER_SIZE = "w185"
        val BIG_POSTER_SIZE = "w780"

        fun getMoviesFromJSON(jsonObject: JSONObject?): ArrayList<Movie>{
            var result: ArrayList<Movie> = arrayListOf()
            if(jsonObject == null) return result
            try {
                var jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
                for(i in 0..jsonArray.length() step 1){
                    var objectMovie  = jsonArray.getJSONObject(i)

                    with(objectMovie){
                        val id = getInt(KEY_ID)
                        val voteCount = getLong(KEY_VOTE_COUNT)
                        val title = getString(KEY_TITLE)
                        val originalTitle = getString(KEY_ORIGINAL_TITLE)
                        val overview = getString(KEY_OVERVIEW)
                        val posterPath = BASE_POSTER_URL +  SMALL_POSTER_SIZE + getString(KEY_POSTER_PATH)
                        val bigPosterPath = BASE_POSTER_URL +  BIG_POSTER_SIZE + getString(KEY_POSTER_PATH)
                        val backdropPath = getString(KEY_BACKDROP_PATH)
                        val voteAverage = getDouble(KEY_VOTE_AVERAGE)
                        val releaseDate = getString(KEY_RELEASE_DATE)
                        val movie = Movie(null, id, voteCount,title, originalTitle, overview, posterPath, bigPosterPath,backdropPath, voteAverage, releaseDate)
                        result.add(movie)
                    }
                }
            } catch (e: Exception) {
            }
            return  result
        }

        fun getReviewsFromJSON(jsonObject: JSONObject?): ArrayList<Review>{
            var result: ArrayList<Review> = arrayListOf()
            if(jsonObject == null) return result
            try {
                var jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
                for(i in 0..jsonArray.length() step 1){
                    var objectReview  = jsonArray.getJSONObject(i)

                    with(objectReview){
                        val author = getString(KEY_AUTHOR)
                        val content = getString(KEY_CONTENT)
                        val review =Review(author, content)
                        result.add(review)
                    }
                }
            } catch (e: Exception) {
            }
            return  result
        }

        fun getTrailersFromJSON(jsonObject: JSONObject?): ArrayList<Trailer>{
            var result: ArrayList<Trailer> = arrayListOf()
            if(jsonObject == null) return result
            try {
                var jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
                for(i in 0..jsonArray.length() step 1){
                    var objectTrailer  = jsonArray.getJSONObject(i)

                    with(objectTrailer){
                        val key = BASE_YOUTUBE_URL + getString(KEY_OF_VIDEO)
                        val name = getString(KEY_NAME)
                        val trailer =Trailer(key, name)
                        result.add(trailer)
                    }
                }
            } catch (e: Exception) {
            }
            return  result
        }


    }
}