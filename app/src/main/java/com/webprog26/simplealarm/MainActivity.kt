package com.webprog26.simplealarm

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.activity.viewModels
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.webprog26.simplealarm.data.Alarm
import com.webprog26.simplealarm.data.AlarmsViewModel
import com.webprog26.simplealarm.data.database.AlarmsDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var alarmsViewModel: AlarmsViewModel

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        alarmsViewModel = AlarmsViewModel(AlarmsDatabase.getDatabase(this).alarmsDao())

        alarmsViewModel.allAlarms.observe(this, Observer { alarms ->
            val alarmsComparator = Comparator<Alarm> { alarm1, alarm2 ->
                if (alarm1.hour == alarm2.hour) {
                    alarm1.minute - alarm2.minute
                } else {
                    alarm1.hour - alarm2.hour
                }
            }
            findViewById<FragmentContainerView>(R.id.fragment_container_view)
                .getFragment<MainFragment>().updateAlarmsList(alarms.sortedWith(alarmsComparator))
        })

        mainViewModel.mButtonClicked.observe(this, Observer {
            val picker =
                MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setHour(12)
                    .setMinute(0).setTitleText(getString(R.string.time_picker_title)).setInputMode(
                        MaterialTimePicker.INPUT_MODE_CLOCK
                    ).build()

            picker.show(supportFragmentManager, "TimePicker")
            // Handle the "OK" button click
            picker.addOnPositiveButtonClickListener {
                val hour = picker.hour
                val minute = picker.minute
                alarmsViewModel.insert(Alarm(0, hour = hour, minute = minute, isActive = true))
            }
        })
    }
}