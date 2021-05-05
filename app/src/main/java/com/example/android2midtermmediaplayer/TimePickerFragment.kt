package com.example.android2midtermmediaplayer

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import java.util.*


class TimePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        return TimePickerDialog(
                activity,
                R.style.themeOnverlay_timePicker,
                activity as OnTimeSetListener?,
                hour,
                minute,
                DateFormat.is24HourFormat(activity)
        )
    }



//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        var calendar : Calendar = Calendar.getInstance()
//        var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
//        var minute: Int = calendar.get(Calendar.MINUTE)
//
//        return TimePickerDialog(
//            activity,
//            activity as OnTimeSetListener,
//            hour,
//            minute,
//            DateFormat.is24HourFormat(activity)
////            android.text.format.DateFormat.is24HourFormat(getActivity())
//        )
//    }
}