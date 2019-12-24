package com.haris.weitani.favoritereceiver

import android.net.Uri

class Helper {
    companion object {
        const val DB_TABLE_NAME = "ResultGetMovie"
        const val AUTHORITY = "com.haris.weitani.moviecataloguesub2.content_provider"
        val CONTENT_URI =
            Uri.Builder().scheme("content")
                .authority(AUTHORITY)
                .appendPath(DB_TABLE_NAME)
                .build()
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"
        const val COLUMN_TITLE = "title"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_POSTER = "poster_path"
        const val COLUMN_POPULARITY = "popularity"
        const val COLUMN_RELEASE_DATE = "release_date"
    }
}