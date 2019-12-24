package com.haris.weitani.moviecataloguesub2.content_provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.haris.weitani.moviecataloguesub2.room.MovieDatabase

class FavoriteMovieContentProvider : ContentProvider() {

    private lateinit var movieDB: MovieDatabase

    companion object {
        const val DB_TABLE_NAME = "ResultGetMovie"
        const val AUTHORITY = "com.haris.weitani.moviecataloguesub2.content_provider"
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        const val FAV_DIR = 1
        const val FAV_ITEM = 2

        init {
            uriMatcher.addURI(AUTHORITY, DB_TABLE_NAME, FAV_DIR)
            uriMatcher.addURI(AUTHORITY, "$DB_TABLE_NAME/#", FAV_ITEM)
        }
    }

    override fun onCreate(): Boolean {
        movieDB = MovieDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val code = uriMatcher.match(p0)
        return if (code == FAV_DIR || code == FAV_ITEM) {
            val context = context ?: return null
            val cursor: Cursor =
                if (code == FAV_DIR)
                    movieDB.movieDao().selectAllFavMovie()
                else movieDB.movieDao().selectById(
                    ContentUris.parseId(p0).toInt()
                )
            cursor.setNotificationUri(context.contentResolver, p0)
            cursor
        } else {
            throw IllegalArgumentException("Unknown Uri: $p0") as Throwable
        }
    }


    override fun insert(p0: Uri, p1: ContentValues?): Uri? = null

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0


    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int = 0


    override fun getType(p0: Uri): String? = null

}