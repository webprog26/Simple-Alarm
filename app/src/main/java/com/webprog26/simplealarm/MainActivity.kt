package com.webprog26.simplealarm

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.activity.viewModels
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
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
            startMaterialTimePickerInternal(            object : OnTimePickerPositiveButtonClickListener {
                override fun onTimePickerPositiveButtonClick(
                    hour: Int,
                    minute: Int
                ) {
                    val defaultAlarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    alarmsViewModel.insert(
                        Alarm(
                            0,
                            hour = hour,
                            minute = minute,
                            "",
                            listOf(),
                            alarmSoundUri = defaultAlarmUri.toString(),
                            isActive = true,
                        )
                    )
                }
            })
        })

        mainViewModel.mAlarmWithPositionClicked.observe(this, Observer { alarm ->
            SingleAlarmEditorFragment.newInstance(alarm)
                .show(supportFragmentManager, "SingleAlarmEditorFragment")
        })

        mainViewModel.mAlarmWithPositionUpdated.observe(this, Observer { alarm ->
            alarmsViewModel.update(alarm)
        })

        mainViewModel.mAlarmDeleted.observe(this, Observer { alarm ->
            alarmsViewModel.delete(alarm)
        })

        mainViewModel.mAlarmTimeClicked.observe(this, Observer { alarm ->
            startMaterialTimePickerInternal(            object : OnTimePickerPositiveButtonClickListener {
                override fun onTimePickerPositiveButtonClick(
                    hour: Int,
                    minute: Int
                ) {
                    alarmsViewModel.update(alarm.copy(
                        hour = hour,
                        minute = minute
                    ))
                }
            })
        })
    }

    private fun startMaterialTimePickerInternal(listener: OnTimePickerPositiveButtonClickListener) {
        startMaterialTimePicker(
            supportFragmentManager, getString(R.string.time_picker_title),
            listener)
    }
}