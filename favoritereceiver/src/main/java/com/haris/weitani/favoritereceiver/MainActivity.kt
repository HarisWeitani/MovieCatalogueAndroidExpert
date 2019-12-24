package com.haris.weitani.favoritereceiver

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var adapter : FavoriteMovieAdapter

    companion object {
        const val MOVIE_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecView()
        LoaderManager.getInstance(this).initLoader(
            MOVIE_CODE,
            null,
            this
        )
    }

    private fun initRecView() {
        rv_movie_list.layoutManager = LinearLayoutManager(applicationContext)
        adapter = FavoriteMovieAdapter()
        rv_movie_list.adapter = adapter
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        if (id == MOVIE_CODE) {
            return CursorLoader(applicationContext, Helper.CONTENT_URI, null, null, null, null)
        } else {
            throw IllegalArgumentException()
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(loader.id == MOVIE_CODE){
            try {
                adapter.setData(data)
            }catch (e:Exception){}
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if(loader.id == MOVIE_CODE)
            adapter.setData(null)
    }
}
