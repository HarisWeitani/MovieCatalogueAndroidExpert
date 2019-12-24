package com.haris.weitani.moviecataloguesub2.widget

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
            FavoriteRemoteViewsFactory(this.applicationContext)
}