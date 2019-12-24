package com.haris.weitani.moviecataloguesub2.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.data.ResultGetMovie
import com.haris.weitani.moviecataloguesub2.room.MovieDatabase

internal class FavoriteRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

//    private val mWidgetItems = ArrayList<Bitmap>()
    private val favMovieList = ArrayList<ResultGetMovie>()
    private lateinit var movieDB : MovieDatabase

    override fun onCreate() {
        val identityToken = Binder.clearCallingIdentity()
        movieDB = MovieDatabase.getDatabase(mContext)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onDataSetChanged() {
/*        //Ini berfungsi untuk melakukan refresh saat terjadi perubahan.
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.poster_ncis))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.poster_naruto_shipudden))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.poster_iron_fist))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.poster_got))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.poster_hanna))*/

        favMovieList.clear()
        favMovieList.addAll(movieDB.movieDao().getAllFavMovie())
    }

    override fun onDestroy() {
        movieDB.close()
    }

    override fun getCount(): Int = favMovieList.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        try {
/*            Picasso.with(mContext)
                .load(GlobalVal.POSTER_BASE_URL + favMovieList[position].poster_path)
                .into(object : Target{
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        Log.d("aaa","onPrepareLoad")
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {
                        Log.d("aaa","onBitmapFailed")
                    }

                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        Log.d("aaa","onBitmapFailed")
                        rv.setImageViewBitmap(R.id.imageView, bitmap)
                    }

                })*/

            val bitmap: Bitmap = Glide.with(mContext)
                .asBitmap()
                .load(GlobalVal.POSTER_BASE_URL + favMovieList[position].poster_path)
                .apply(RequestOptions().fitCenter())
                .submit(800, 550)
                .get()

            rv.setImageViewBitmap(R.id.imageView, bitmap)
        }catch (e:Exception){}

        val extras = bundleOf(
                FavoriteBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}