package com.haris.weitani.moviecataloguesub2.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class ResponseGetMovie(
    var page: Int? = 0,
    var results: List<ResultGetMovie?>? = listOf(),
    var total_pages: Int? = 0,
    var total_results: Int? = 0
)

@Parcelize
@Entity
data class ResultGetMovie(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "adult" )var adult: Boolean? = false,
    @ColumnInfo(name = "backdrop_path")var backdrop_path: String? = "",
    @ColumnInfo(name = "original_language")var original_language: String? = "",
    @ColumnInfo(name = "original_title")var original_title: String? = "",
    @ColumnInfo(name = "overview")var overview: String? = "",
    @ColumnInfo(name = "popularity")var popularity: Double? = 0.0,
    @ColumnInfo(name = "poster_path")var poster_path: String? = "",
    @ColumnInfo(name = "release_date")var release_date: String? = "",
    @ColumnInfo(name = "title")var title: String? = "",
    @ColumnInfo(name = "video")var video: Boolean? = false,
    @ColumnInfo(name = "vote_average")var vote_average: Double? = 0.0,
    @ColumnInfo(name = "vote_count")var vote_count: Int? = 0,
    @ColumnInfo(name = "is_favorite") var is_favorite : Boolean? = false
): Parcelable