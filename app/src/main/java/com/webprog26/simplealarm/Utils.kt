package com.webprog26.simplealarm

import android.content.ContentResolver
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.webprog26.simplealarm.data.Alarm
import java.io.File

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
    val picker =
        MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(initialHour)
            .setMinute(initialMinute).setTitleText(title).setInputMode(
                MaterialTimePicker.INPUT_MODE_CLOCK
            ).build().also {
                it.show(supportFragmentManager, "TimePicker")
            }
    // Handle the "OK" button click
        picker.addOnPositiveButtonClickListener {
            onTimePickerPositiveButtonListener.onTimePickerPositiveButtonClick(picker.hour, picker.minute)
        }
}

fun getTitleFromUri(context: Context, uri: Uri): String? {
    // 1. Handle System Settings / Alarms (content://settings/system/...)
    if (uri.toString().contains("settings/system")) {
        return RingtoneManager.getRingtone(context, uri)?.getTitle(context)
    }

    // 2. Handle MediaStore URIs (content://media/external...)
    // We use a try-catch because some URIs look like MediaStore but lack the column
    return try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                // Try 'title' first (Metadata title)
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                // Fallback to '_display_name' (The actual filename)
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                when {
                    titleIndex != -1 -> cursor.getString(titleIndex)
                    displayNameIndex != -1 -> cursor.getString(displayNameIndex)
                    else -> uri.lastPathSegment // Last resort: just the ID number
                }
            } else null
        }
    } catch (e: Exception) {
        // 3. Last Resort Fallback: RingtoneManager works for almost all audio URIs
        RingtoneManager.getRingtone(context, uri)?.getTitle(context)
    }
}