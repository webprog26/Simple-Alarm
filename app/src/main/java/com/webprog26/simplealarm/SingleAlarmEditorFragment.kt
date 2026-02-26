package com.webprog26.simplealarm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.ChipGroup
import com.webprog26.simplealarm.data.Alarm
import kotlin.getValue

class SingleAlarmEditorFragment : BottomSheetDialogFragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single_alarm_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mTvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time)

        val mEtAlarmName = view.findViewById<EditText>(R.id.et_alarm_name);

        val mAlarmData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(KEY_ALARM, Alarm::class.java)
        } else {
            arguments?.getParcelable<Alarm>(KEY_ALARM)
        }

        mAlarmData?.let {

            mEtAlarmName.setText(mAlarmData.alarmName)

            var mUpdatedAlarm: Alarm = mAlarmData.copy()

            val mAlarmDaysSelectedIds = mutableListOf<Int>()

            mAlarmDaysSelectedIds.addAll(mUpdatedAlarm.alarmDaysSelectedIds)

            mTvAlarmTime.text = mAlarmData.getTimeString()
            view.findViewById<TextView>(R.id.tv_alarm_state).text = if (mAlarmData.isActive) {
                getString(R.string.alarm_state_on)
            } else {
                getString(R.string.alarm_state_off)
            }

            view.findViewById<Button>(R.id.btn_edit_alarm).setOnClickListener { view ->
                startMaterialTimePicker(
                    requireActivity().supportFragmentManager,
                    getString(R.string.time_picker_title),
                    object : OnTimePickerPositiveButtonClickListener {
                        override fun onTimePickerPositiveButtonClick(
                            hour: Int,
                            minute: Int
                        ) {
                            mUpdatedAlarm = mUpdatedAlarm.copy(hour = hour, minute = minute)
                            mTvAlarmTime.text = mUpdatedAlarm.getTimeString()

                        }
                    }, mAlarmData.hour, mAlarmData.minute
                )
            }

            view.findViewById<Button>(R.id.btn_save_alarm).setOnClickListener {

                mUpdatedAlarm = mUpdatedAlarm.copy(alarmDaysSelectedIds = mAlarmDaysSelectedIds, alarmName = mEtAlarmName.text.toString())
                mainViewModel.onAlarmUpdated(mUpdatedAlarm/*, mAlarmPosition*/)

                dismiss()
            }

            view.findViewById<Button>(R.id.btn_delete_alarm).setOnClickListener { view ->
                mainViewModel.onAlarmDeleted(mAlarmData)
                dismiss()
            }

            val chipGroupAlarmDays = view.findViewById<ChipGroup>(R.id.chip_group_alarm_days)

            if (!mAlarmDaysSelectedIds.isEmpty()) {
                mAlarmDaysSelectedIds.forEach { chipId ->
                    chipGroupAlarmDays.check(chipId)
                }
            }

            chipGroupAlarmDays.setOnCheckedStateChangeListener { group, checkedIds ->
                mAlarmDaysSelectedIds.clear()
                mAlarmDaysSelectedIds.addAll(checkedIds)

                val mDaysMap = mutableMapOf<String, Boolean>(
                    MONDAY to false,
                    TUESDAY to false,
                    WEDNESDAY to false,
                    THURSDAY to false,
                    FRIDAY to false,
                    SATURDAY to false,
                    SUNDAY to false
                )

                var mAlarmDaysSelectedNames = ""

                checkedIds.forEach { id->
                    when(id) {
                        R.id.chip_monday -> mDaysMap.put(MONDAY, true)
                        R.id.chip_tuesday -> mDaysMap.put(TUESDAY, true)
                        R.id.chip_wednesday -> mDaysMap.put(WEDNESDAY, true)
                        R.id.chip_thursday -> mDaysMap.put(THURSDAY, true)
                        R.id.chip_friday -> mDaysMap.put(FRIDAY, true)
                        R.id.chip_saturday -> mDaysMap.put(SATURDAY, true)
                        R.id.chip_sunday -> mDaysMap.put(SUNDAY, true)
                    }
                }

                if (mDaysMap[MONDAY] == true && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == false && mDaysMap[FRIDAY] == false && mDaysMap[SATURDAY] == false && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_monday)}-${getString(R.string.alarm_scheduled_wednesday)}"
                } else if (mDaysMap[MONDAY] == true && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == false && mDaysMap[SATURDAY] == false && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_monday)}-${getString(R.string.alarm_scheduled_thursday)}"
                } else if (mDaysMap[MONDAY] == true && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == false && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_monday)}-${getString(R.string.alarm_scheduled_friday)}"
                } else if (mDaysMap[MONDAY] == true && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_monday)}-${getString(R.string.alarm_scheduled_saturday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == false && mDaysMap[SATURDAY] == false && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_tuesday)}-${getString(R.string.alarm_scheduled_thursday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == false && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_tuesday)}-${getString(R.string.alarm_scheduled_friday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_tuesday)}-${getString(R.string.alarm_scheduled_saturday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == true && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == true) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_tuesday)}-${getString(R.string.alarm_scheduled_sunday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == false && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == false && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_wednesday)}-${getString(R.string.alarm_scheduled_friday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == false && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_wednesday)}-${getString(R.string.alarm_scheduled_saturday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == false && mDaysMap[WEDNESDAY] == true && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == true) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_wednesday)}-${getString(R.string.alarm_scheduled_sunday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == false && mDaysMap[WEDNESDAY] == false && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == false) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_thursday)}-${getString(R.string.alarm_scheduled_saturday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == false && mDaysMap[WEDNESDAY] == false && mDaysMap[THURSDAY] == true && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == true) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_thursday)}-${getString(R.string.alarm_scheduled_sunday)}"
                } else if (mDaysMap[MONDAY] == false && mDaysMap[TUESDAY] == false && mDaysMap[WEDNESDAY] == false && mDaysMap[THURSDAY] == false && mDaysMap[FRIDAY] == true && mDaysMap[SATURDAY] == true && mDaysMap[SUNDAY] == true) {
                    mAlarmDaysSelectedNames = "${getString(R.string.alarm_scheduled_friday)}-${getString(R.string.alarm_scheduled_sunday)}"
                } else {
                    checkedIds.forEach { id->
                        when(id) {
                            R.id.chip_monday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_monday) + " "
                            R.id.chip_tuesday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_tuesday) + " "
                            R.id.chip_wednesday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_wednesday) + " "
                            R.id.chip_thursday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_thursday) + " "
                            R.id.chip_friday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_friday) + " "
                            R.id.chip_saturday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_saturday) + " "
                            R.id.chip_sunday -> mAlarmDaysSelectedNames += getString(R.string.alarm_scheduled_sunday) + " "
                        }
                    }
                }

                mUpdatedAlarm = mUpdatedAlarm.copy(alarmDaysSelectedNames = mAlarmDaysSelectedNames)
            }
        }
    }

    companion object {
        const val KEY_ALARM = "key_alarm"
        const val KEY_POSITION = "key_position"

        const val MONDAY = "Monday"
        const val TUESDAY = "Tuesday"
        const val WEDNESDAY = "Wednesday"
        const val THURSDAY = "Thursday"
        const val FRIDAY = "Friday"
        const val SATURDAY = "Saturday"
        const val SUNDAY = "Sunday"


        fun newInstance(alarm: Alarm): SingleAlarmEditorFragment {
            return SingleAlarmEditorFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(KEY_ALARM, alarm)
                arguments = bundle
            }
        }
    }
}