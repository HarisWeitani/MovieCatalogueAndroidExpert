package com.haris.weitani.moviecataloguesub2.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.common.SharedPreference
import kotlinx.android.synthetic.main.activity_notification_settings.*

class NotificationSettingsActivity : AppCompatActivity() {

    private lateinit var sharePref : SharedPreference
    private lateinit var notificationReceiver: NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharePref = SharedPreference(this)
        notificationReceiver = NotificationReceiver()

        initAction()

    }

    fun initAction(){
        sw_daily_reminder.isChecked = sharePref.getValueBoolean(GlobalVal.DAILY_REMINDER_SP, false)
        sw_daily_reminder.setOnCheckedChangeListener { _, isChecked ->
            sharePref.save(GlobalVal.DAILY_REMINDER_SP,isChecked)
            if(isChecked){
                notificationReceiver.setDailyReminder(this)
            }else{
                notificationReceiver.cancelDailyReminder(this)
            }
        }

        sw_release_today.isChecked = sharePref.getValueBoolean(GlobalVal.RELEASE_TODAY_REMINDER_SP,false)
        sw_release_today.setOnCheckedChangeListener { _, isChecked ->
            sharePref.save(GlobalVal.RELEASE_TODAY_REMINDER_SP,isChecked)
            if(isChecked){
                notificationReceiver.setReleaseTodayReminder(this)
            }else{
                notificationReceiver.cancelReleaseToday(this)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> {
                this.finish()
            }
        }

        return true
    }
}
