package com.haris.weitani.moviecataloguesub2.main_activity

import android.R
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.BuildConfig
import com.haris.weitani.moviecataloguesub2.common.API
import com.haris.weitani.moviecataloguesub2.data.*
import com.haris.weitani.moviecataloguesub2.room.MovieDatabase
import com.haris.weitani.moviecataloguesub2.room.TVShowsDatabase
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    companion object {
        lateinit var movieDB: MovieDatabase
        lateinit var tvshowDB: TVShowsDatabase
    }

    private var apiService: API? = null
    private val listMovie = MutableLiveData<ArrayList<ResultGetMovie>>()
    private val listTvShows = MutableLiveData<ArrayList<ResultTvShow>>()

    internal fun setMovie(context: Context) {
        apiService = API.networkApi()

        apiService!!.getPopularMovies(BuildConfig.API_KEY)
            .enqueue(object : Callback<ResponseGetMovie> {
                override fun onFailure(call: Call<ResponseGetMovie>, t: Throwable) {
                    Log.d(GlobalVal.NETWORK_LOG, t.localizedMessage)
                    showDialog(context)
                }

                override fun onResponse(
                    call: Call<ResponseGetMovie>,
                    response: Response<ResponseGetMovie>
                ) {
                    Log.d(GlobalVal.NETWORK_LOG, response.message())
                    DAO.responseGetMovie = response.body()
                    saveMovieToDB()
                }
            })
    }

    private fun saveMovieToDB(){
        doAsync {
            for ( data in DAO.responseGetMovie!!.results!!) {
                movieDB.movieDao().insertMovie(data!!)
            }
            listMovie.postValue(movieDB.movieDao().getAllMovie() as ArrayList<ResultGetMovie>?)
        }
    }

    internal fun refreshMovieData(){
        doAsync {
            listMovie.postValue(movieDB.movieDao().getAllMovie() as ArrayList<ResultGetMovie>?)
        }
    }

    internal fun getMovie(): LiveData<ArrayList<ResultGetMovie>> {
        return listMovie
    }

    internal fun setTvShows(context: Context) {
        apiService = API.networkApi()

        apiService!!.getPopularTvShow(BuildConfig.API_KEY)
            .enqueue(object : Callback<ResponseTvShows> {
                override fun onFailure(call: Call<ResponseTvShows>, t: Throwable) {
                    Log.d(GlobalVal.NETWORK_LOG, t.localizedMessage)
                    showDialog(context)
                }

                override fun onResponse(
                    call: Call<ResponseTvShows>,
                    response: Response<ResponseTvShows>
                ) {
                    Log.d(GlobalVal.NETWORK_LOG, response.message())
                    DAO.responseGetTvShows = response.body()
                    saveTvShowsToDB()

                }
            })
    }

    private fun saveTvShowsToDB(){
        doAsync {
            for ( data in DAO.responseGetTvShows!!.results!!) {
                tvshowDB.tvShowsDao().insertTvShows(data!!)
            }
            listTvShows.postValue(tvshowDB.tvShowsDao().getAllTvShows() as ArrayList<ResultTvShow>?)
        }
    }

    internal fun refreshTvShowsData(){
        doAsync {
            listTvShows.postValue(tvshowDB.tvShowsDao().getAllTvShows() as ArrayList<ResultTvShow>?)
        }
    }

    internal fun getTvShows(): LiveData<ArrayList<ResultTvShow>> {
        return listTvShows
    }

    internal fun searchMovie(context: Context,keyword : String){
        apiService = API.networkApi()
        apiService!!.searchMovies(BuildConfig.API_KEY,keyword)
            .enqueue(object : Callback<ResponseGetMovie>{
                override fun onFailure(call: Call<ResponseGetMovie>, t: Throwable) {
                    Log.d(GlobalVal.NETWORK_LOG, t.localizedMessage)
                    showDialog(context)
                }

                override fun onResponse(
                    call: Call<ResponseGetMovie>,
                    response: Response<ResponseGetMovie>
                ) {
                    Log.d(GlobalVal.NETWORK_LOG, response.message())
                    listMovie.postValue(response.body()?.results as ArrayList<ResultGetMovie>?)
                }
            })
    }

    private fun showDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Fetching Data Failed")
            .setMessage("Please Check Your Internet Connection And Re-open My App")
            .setPositiveButton(R.string.yes, null)
            .setIcon(R.drawable.ic_dialog_alert)
            .show()
    }

}