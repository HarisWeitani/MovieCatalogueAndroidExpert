package com.haris.weitani.moviecataloguesub2.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class ResponseTvShows(
    var page: Int? = 0,
    var results: List<ResultTvShow?>? = listOf(),
    var total_pages: Int? = 0,
    var total_results: Int? = 0
)

@Parcelize
@Entity
data class ResultTvShow(
    @PrimaryKey var id: Int? = 0,
    @ColumnInfo(name = "backdrop_path") var backdrop_path: String? = "",
    @ColumnInfo(name = "first_air_date") var first_air_date: String? = "",
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "original_language") var original_language: String? = "",
    @ColumnInfo(name = "original_name") var original_name: String? = "",
    @ColumnInfo(name = "overview") var overview: String? = "",
    @ColumnInfo(name = "popularity") var popularity: Double? = 0.0,
    @ColumnInfo(name = "poster_path") var poster_path: String? = "",
    @ColumnInfo(name = "vote_average") var vote_average: Double? = 0.0,
    @ColumnInfo(name = "vote_count") var vote_count: Int? = 0,
    @ColumnInfo(name = "is_favorite") var is_favorite : Boolean? = false
): Parcelable