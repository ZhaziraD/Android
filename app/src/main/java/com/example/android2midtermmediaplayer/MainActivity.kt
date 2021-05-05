package com.example.android2midtermmediaplayer

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var listenMusicSecond: Long = 60 //4 * 60 = 4minutes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            var channel = NotificationChannel(
//                    "My Notification",
//                    "My Notification",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            )
//
//            val manager = getSystemService(
//                    NotificationManager::class.java
//            )
//            manager.createNotificationChannel(channel)
//        }





        button.setOnClickListener{
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)


            ///////////////////////////////////////////////
//            val timeAtClickButton = System.currentTimeMillis()
//            val alarmStartTime: Long = listenMusicSecond * 1000
//
//            var intentTimer = Intent(this, ReminderBroadcast::class.java)
//            var pendingIntent = PendingIntent.getBroadcast(
//                    this, 0, intentTimer, 0)
//
//            var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//
//            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtClickButton + alarmStartTime, pendingIntent)
            ///////////////////////////////////////////////
        }
    }
}