package com.haris.weitani.moviecataloguesub2.notification

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.BuildConfig
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.common.API
import com.haris.weitani.moviecataloguesub2.data.ResponseGetMovie
import com.haris.weitani.moviecataloguesub2.data.ResultGetMovie
import com.haris.weitani.moviecataloguesub2.main_activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class NotificationReceiver : BroadcastReceiver() {

    var apiService: API? = null

    companion object {
        private const val EXTRA_TYPE = "type"
        private const val TYPE_DAILY = "TYPE_DAILY"
        private const val TYPE_RELEASE = "TYPE_RELEASE"
        private const val ID_DAILY_REMINDER = 2211
        private const val ID_RELEASE_TODAY = 1122
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        if (type == TYPE_DAILY)
            showDailyReminder(context)
        else if (type == TYPE_RELEASE)
            getReleaseToday(context)
    }

    fun cancelDailyReminder(context: Context) {
        cancelReminder(context, TYPE_DAILY)
    }

    fun cancelReleaseToday(context: Context) {
        cancelReminder(context, TYPE_RELEASE)
    }

    fun setReleaseTodayReminder(context: Context) {
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                ID_RELEASE_TODAY,
                getReminderIntent(context, TYPE_RELEASE),
                0
            )
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            getReminderTime(TYPE_RELEASE).timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun setDailyReminder(context: Context) {
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                ID_DAILY_REMINDER,
                getReminderIntent(context, TYPE_DAILY),
                0
            )
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            getReminderTime(TYPE_DAILY).timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun getReminderTime(type: String): Calendar {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] =
            if (type == TYPE_DAILY) 7 else 8
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        return calendar
    }

    private fun getReminderIntent(context: Context, type: String): Intent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)
        return intent
    }

    private fun getReleaseToday(context: Context) {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val now = dateFormat.format(date)

        apiService = API.networkApi()
        apiService!!.getReleasedMovies(BuildConfig.API_KEY, now, now)
            .enqueue(object : Callback<ResponseGetMovie> {
                override fun onFailure(call: Call<ResponseGetMovie>, t: Throwable) {
                    Log.d(GlobalVal.NETWORK_LOG, t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ResponseGetMovie>,
                    response: Response<ResponseGetMovie>
                ) {
                    Log.d(GlobalVal.NETWORK_LOG, response.message())
                    if (response.code() == 200 && !response.body()?.results.isNullOrEmpty()) {
                        var id = 2
                        for (data in response.body()?.results as ArrayList<ResultGetMovie>) {
                            showReleaseToday(
                                context,
                                data.title.toString(),
                                data.overview.toString(),
                                id
                            )
                            id++
                        }
                    }
                }
            })
    }

    private fun showReleaseToday(context: Context, title: String, content: String, id: Int) {

        val CHANNEL_ID = "CH_01"
        val CHANNEL_NAME = "TODAY_RELEASE_CH"
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_favorite_red_24dp
                    )
                )
                .setSmallIcon(R.drawable.ic_favorite_red_24dp)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notification: Notification = mBuilder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        mNotificationManager.notify(id, notification)
    }

    private fun showDailyReminder(context: Context) {
        val NOTIFICATION_ID = 1
        val CHANNEL_ID = "CH_01"
        val CHANNEL_NAME = "DAILY_REMINDER_CH"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                context, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.ic_favorite_red_24dp
                    )
                )
                .setSmallIcon(R.drawable.ic_favorite_red_24dp)
                .setContentTitle(context.resources.getString(R.string.app_name))
                .setContentText(context.resources.getString(R.string.daily_reminder_msg))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        val notification: Notification = mBuilder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }


    private fun cancelReminder(context: Context, type: String) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val requestCode =
            if (type == TYPE_DAILY) ID_DAILY_REMINDER else ID_RELEASE_TODAY
        val pendingIntent =
            PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

}