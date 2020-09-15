package com.idonse.mymovies.utils

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
    companion object{

        private val BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos"
        private val BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews"
        //https://api.themoviedb.org/3/movie/550?api_key=30069bf7c511b3e049efe335024310ed
        private val BASE_URL = "https://api.themoviedb.org/3/discover/movie"

        private val PARAMS_API_KEY = "api_key"
        private val PARAMS_LANGUAGE = "language"
        private val PARAMS_SORT_BY = "sort_by"
        private val PARAMS_PAGE = "page"
        private val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"

        private val API_KEY = "30069bf7c511b3e049efe335024310ed"
//        private val LANGUAGE_VALUE = "ru-RU"
        private val SORT_BY_POPULARITY = "popularity.desc"
        private val SORT_BY_TOP_RATED = "vote_average.desc"

        val POPULARITY = 0
        val TOP_RATED = 1

        private val MIN_VOTE_COUNT_VALUE = "1000"


        fun buildURLReviews(id:Int, lang:String): URL?{
            var uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang).build()
            try {
                return URL(uri.toString())
            } catch (e: Exception) {
            }
            return null
        }

        fun buildURLVideos(id:Int, lang:String): URL?{
            var uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang).build()
            try {
                return URL(uri.toString())
            } catch (e: Exception) {
            }
            return null
        }

        fun buildURL(sortBy: Int, page: Int, lang:String): URL? {
            var result: URL? = null
            var methodOfSort: String = if(sortBy == POPULARITY) SORT_BY_POPULARITY else SORT_BY_TOP_RATED
            var uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, lang)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, page.toString())
                .build()
            try {
                result = URL(uri.toString())
            } catch (e: Exception) {
            }
            return  result
        }

        fun getJSONForVideos(id: Int, lang:String): JSONObject?{
            var result: JSONObject? = null
            var url = buildURLVideos(id, lang)
            try {
                result = JSONLoadTask().execute(url).get()
            } catch (e: Exception) {
            }
            return result
        }

        fun getJSONForReviews(id: Int, lang:String): JSONObject?{
            var result: JSONObject? = null
            var url = buildURLReviews(id, lang)
            try {
                result = JSONLoadTask().execute(url).get()
            } catch (e: Exception) {
            }
            return result
        }


        fun getJSONFromNetwork(sortBy: Int, page: Int, lang:String): JSONObject?{
            var result: JSONObject? = null
            var url = buildURL(sortBy, page, lang)
            try {
                result = JSONLoadTask().execute(url).get()
            } catch (e: Exception) {
            }
            return result
        }

        class JSONLoadTask : AsyncTask<URL?, Unit, JSONObject?>() {
            override fun doInBackground(vararg p0: URL?): JSONObject? {
                var result: JSONObject? = null
                if(p0.isEmpty()){return result }
                var connection: HttpURLConnection? = null
                var builder = StringBuilder()
                try {
                    connection = p0[0]?.openConnection() as HttpURLConnection
                    var reader = BufferedReader(InputStreamReader(connection.inputStream))
                    var line = reader.readLine()
                    while (line!= null){
                        builder.append(line)
                        line = reader.readLine()
                    }
                    result = JSONObject(builder.toString())

                } catch (e: Exception) {
                } finally {
                    connection?.disconnect()
                }
                return result

            }
        }

        class JSONLoader(context: Context, bundle: Bundle?) : AsyncTaskLoader<JSONObject>(context) {
            private var bundle:Bundle? = bundle
            var onStartLoadingListener:OnStartLoadingListener? = null

            interface OnStartLoadingListener{
                fun onStartLoading()
            }

            override fun onStartLoading() {
                super.onStartLoading()
                onStartLoadingListener?.onStartLoading()
                forceLoad()
            }

            override fun loadInBackground(): JSONObject? {
                var urlAsString = bundle?.getString("url")
                var url:URL? = null
                try {
                    url = URL(urlAsString)
                } catch (e: Exception) {
                }
                if(url == null) return null
                var result: JSONObject? = null
                var connection: HttpURLConnection? = null
                var builder = StringBuilder()
                try {
                    connection = url.openConnection() as HttpURLConnection
                    var reader = BufferedReader(InputStreamReader(connection.inputStream))
                    var line = reader.readLine()
                    while (line!= null){
                        builder.append(line)
                        line = reader.readLine()
                    }
                    result = JSONObject(builder.toString())

                } catch (e: Exception) {
                } finally {
                    connection?.disconnect()
                }
                return result
            }
        }

    }


}