package com.example.android2midtermmediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntentOld
import android.content.IntentFilter
import android.media.MediaPlayer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class ReminderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
//        var songActivity = SongActivity()
//        var msg = songActivity.timeSet
        var msg = "Time is Up!!!" //+ hourOfDay + ":" + minute

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            context, "My Notification"
        )

        builder.setContentTitle("Notifiction")
        builder.setContentText(msg)
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_24)
        builder.setAutoCancel(true)

        /////////////////////////////////////////
        var managerCompat = NotificationManagerCompat.from(context)
        managerCompat.notify(0, builder.build())

        var player =  MediaPlayer.create(context, R.raw.press_start)

        player!!.isLooping = false // Sets the player to be looping or non-looping.
        player!!.start() // Starts Playback.
    }
}



//var builder: NotificationCompat.Builder = NotificationCompat.Builder(
//    context,
//    "My Notification"
//)
//
//builder.setContentTitle("Notifiction")
//builder.setContentText(msg)
//builder.setSmallIcon(R.drawable.ic_baseline_menu_24)
//builder.setAutoCancel(true)
