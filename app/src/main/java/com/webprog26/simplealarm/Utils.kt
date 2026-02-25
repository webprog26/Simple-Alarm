package com.webprog26.simplealarm

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.webprog26.simplealarm.data.Alarm

fun Alarm.getTimeString() : String {
    return "${hour.maybeAdjustTimeValue()} : ${minute.maybeAdjustTimeValue()}"
}

private fun Int.maybeAdjustTimeValue(): String {
    return if (this >= 0 && this < 10) {
        "0${this}"
    } else {
        this.toString()
    }
}

interface OnTimePickerPositiveButtonClickListener {
    fun onTimePickerPositiveButtonClick(hour: Int, minute: Int)
}

fun startMaterialTimePicker(supportFragmentManager: FragmentManager,
                            title: String,
                            onTimePickerPositiveButtonListener: OnTimePickerPositiveButtonClickListener,
                            initialHour: Int = 12,
                            initialMinute: Int = 0,) {
    val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(initialHour)
            .setMinute(initialMinute).setTitleText(title).setInputMode(
                MaterialTimePicker.INPUT_MODE_CLOCK
            ).build()
        picker.show(supportFragmentManager, "TimePicker")
        // Handle the "OK" button click
        picker.addOnPositiveButtonClickListener {
            onTimePickerPositiveButtonListener.onTimePickerPositiveButtonClick(picker.hour, picker.minute)
        }

}