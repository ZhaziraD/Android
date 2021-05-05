package com.example.android2midtermmediaplayer

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_song.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class SongActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private var restTimer: CountDownTimer?= null
    private var restProgress = 0
    private var mTimerRunning: Boolean = false
    private var currentDur: Long = 0
    var currentPlayer: MediaPlayer? = null
    private var mediaCount = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        leftToRight.bringToFront()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(
                    "My Notification",
                    "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(
                    NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
        }

        btnTimer.setOnClickListener(View.OnClickListener {
            val timePicker: DialogFragment = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker")
        })



        //BACK ACTION BAR
        toolbar_exercise_activity.bringToFront()
        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
            setToNull()
            currentPlayer!!.stop()
        }

        btnLeft.setOnClickListener{
            currentPlayer!!.stop()
            currentPlayer = null
            setToNull()
            setupRestView(currentID - 1)
        }

        btnRight.setOnClickListener{
            currentPlayer!!.stop()
            currentPlayer = null
            setToNull()
            setupRestView(currentID + 1)
        }


        btnStopPlay.setOnClickListener {
            if (mTimerRunning) {
                pauseTimer()
            } else {
                setRestProgressBar(currentID)
            }
        }


        setupRestView(1)
        updateCountDownText()
    }

//    fun setUpRecyclerView() {
//        val adapter = AbsListView.RecyclerListener
//    }

    private fun setToNull() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
    }





    private fun setRestProgressBar(id: Int) {
        if (id <= mediaCount) {
            if (id == 1) {
                btnRight.show()
                btnLeft.hide()
            } else if (id == mediaCount) {
                btnLeft.show()
                btnRight.hide()
            } else {
                btnRight.show()
                btnLeft.show()
            }

            var songDuration: Int = setSongDuration(id)

            progressBar.max = songDuration
            progressBar.progress = songDuration
            songArtist.text = setSongArtist(id)
            songLyrics.text = setSongLyrics(id)
//            songDate.text = setSongDate(id)
            songIcon.setImageResource(setSongImage(id))
            songName.text = setSongName(id)


            //MUSIC STARTS
            if (currentPlayer == null) {
                currentPlayer =  MediaPlayer.create(applicationContext, setSongMp3(id))
            }
            if(currentPlayer!!.isPlaying) {
                return
            }

//            currentPlayer!!.start()
            try {
                currentPlayer!!.isLooping = false
                currentPlayer!!.start()
            } catch (e: Exception) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
//            currentPlayer = player







            restTimer = object : CountDownTimer((songDuration * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    restProgress++
                    progressBar.progress = songDuration - restProgress
                    //not needable
//                    tvTimer.text = (songDuration - restProgress).toString()
                    currentDur = (songDuration * 1000).toLong()
                    currentDur = millisUntilFinished

                    updateCountDownText()


                }

                override fun onFinish() {
                    mTimerRunning = false
                    btnStopPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    ///////////////// player
                    currentPlayer!!.stop()
                    currentPlayer = null
                    setupRestView(id + 1)
                }
            }.start()
            mTimerRunning = true
            btnStopPlay.setImageResource(R.drawable.ic_baseline_pause_24)

        }
    }

    fun pauseTimer() {
        currentPlayer!!.pause()
        restTimer!!.cancel()
        mTimerRunning = false
        btnStopPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    fun updateCountDownText() {
        val minutes = (currentDur / 1000) / 60 as Int
        val seconds = (currentDur / 1000) % 60 as Int

        val timeLeftFormatted: String =
                String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        tvTimer.setText(timeLeftFormatted)
    }


    private var currentID: Int = 2
    private fun setupRestView(id: Int){
        currentID = id
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        setRestProgressBar(id)
    }

    //IMAGES
    fun setSongImage(imageID: Int): Int {
        return when(imageID) {
            1 -> R.drawable.im_habits
            2 -> R.drawable.im_puttin_on_the_ritz
            3 -> R.drawable.im_pyt
            4 -> R.drawable.im_it_just_is
            5 -> R.drawable.im_smb_i_used_to_know
            6 -> R.drawable.im_so_beautiful
            else -> 0
        }
    }

    //NAMES
    fun setSongName(nameID: Int): String {
        return when(nameID) {
            1 -> "HABITS"
            2 -> "PUTTIN' ON THE RITZ"
            3 -> "PRETTY YOUNG THING"
            4 -> "IT JUST IS"
            5 -> "SOMEBODY THAT I USED TO KNOW"
            6 -> "SO BEAUTIFUL"
            else -> ""
        }
    }

    //ARTIST
    fun setSongArtist(artistID: Int): String {
        return when(artistID) {
            1 -> "Mr. Kitty"
            2 -> "Frederick Frankenstein"
            3 -> "Michael Jackson"
            4 -> "eaJ X Seori (Feat. keshi's strat)"
            5 -> "Glee Cast"
            6 -> "DPR IAN"
            else -> ""
        }
    }

    //LYRICS
    fun setSongLyrics(lyricsID: Int): String {
        val array = ByteArrayOutputStream()
        //access to the txt file
        var reader: InputStream = resources.openRawResource(setSongTxt(lyricsID))
        var char: Int?

        try {
            char = reader.read()
            while (char != -1) {
                if (char != null) {
                    array.write(char)
                }
                char = reader.read()
            }
            reader.close()
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

        return array.toString()
    }

    //TXT
    fun setSongTxt(txtID: Int): Int {
        return when(txtID) {
            1 -> R.raw.txthabits
            2 -> R.raw.txtputtinontheritz
            3 -> R.raw.txtpyt
            4 -> R.raw.txtitjustis
            5 -> R.raw.txtsmbthatiusedtoknow
            6 -> R.raw.txtsobeautiful
            else -> 0
        }
    }

    //MP3
    fun setSongMp3(mp3ID: Int): Int {
        return when(mp3ID) {
            1 -> R.raw.habits
            2 -> R.raw.puttin_on_the_ritz
            3 -> R.raw.pyt
            4 -> R.raw.it_just_is
            5 -> R.raw.smb_i_used_to_know
            6 -> R.raw.so_beautiful
            else -> 0
        }
    }

    //DURATION
    fun setSongDuration(durationID: Int): Int {
        var player: MediaPlayer
        var dur: Int

        when (durationID) {
            1 -> {
                player = MediaPlayer.create(applicationContext, setSongMp3(1))
                dur = player.duration.div(1000)
            }
            2 -> {
                player = MediaPlayer.create(applicationContext, setSongMp3(2))
                dur = player.duration.div(1000)
            }
            3 -> {
                player = MediaPlayer.create(applicationContext, setSongMp3(3))
                dur = player.duration.div(1000)
            }
            4 -> {
                player = MediaPlayer.create(applicationContext, setSongMp3(4))
                dur = player.duration.div(1000)
            }
            5 -> {
                player = MediaPlayer.create(applicationContext, setSongMp3(5))
                dur = player.duration.div(1000)
            }
            6 -> {
                player = MediaPlayer.create(applicationContext, setSongMp3(6))
                dur = player.duration.div(1000)
            }
            else -> {
                dur = 0
            }
        }
        return dur
    }


//    public var timeSet: String? = null
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//        timeSet = "Time is up! " + hourOfDay + ":" + minute + " is now!"
        Toast.makeText(this, "The time is set to " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show()

        ///////////////////////////////////////////////////////////////////
        var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val startTime: Calendar = Calendar.getInstance()
        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        startTime.set(Calendar.MINUTE, minute)
        startTime.set(Calendar.SECOND, 0)
        val alarmStartTime: Long = startTime.getTimeInMillis()


        var intent = Intent(this, ReminderBroadcast::class.java)
        var pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, 0
        )

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent)
        ///////////////////////////////////////////////
    }
}