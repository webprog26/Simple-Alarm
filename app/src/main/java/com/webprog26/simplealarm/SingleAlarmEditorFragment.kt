package com.webprog26.simplealarm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

        val tvAlarmTime = view.findViewById<TextView>(R.id.tv_alarm_time);

        val alarmData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(KEY_ALARM, Alarm::class.java)
        } else {
            arguments?.getParcelable<Alarm>(KEY_ALARM)
        }
        val alarmPosition = arguments?.getInt(KEY_POSITION)

        alarmData?.let {

            var updatedAlarm: Alarm? = null

            tvAlarmTime.text = alarmData.getTimeString()
            view.findViewById<TextView>(R.id.tv_alarm_state).text = if (alarmData.isActive) {
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
                            updatedAlarm = alarmData.copy(hour = hour, minute = minute)
                            tvAlarmTime.text = updatedAlarm.getTimeString()

                        }
                    }, alarmData.hour, alarmData.minute
                )
            }

            view.findViewById<Button>(R.id.btn_save_alarm).setOnClickListener {
                alarmPosition?.let {
                    if (updatedAlarm != null) {
                        updatedAlarm.let {
                            mainViewModel.onAlarmUpdated(updatedAlarm, alarmPosition)
                            dismiss()
                        }
                    } else {
                        dismiss()
                    }
                }
            }

            view.findViewById<Button>(R.id.btn_delete_alarm).setOnClickListener { view ->
                mainViewModel.onAlarmDeleted(alarmData)
                dismiss()
            }
        }
    }

    companion object {
        const val KEY_ALARM = "key_alarm"
        const val KEY_POSITION = "key_position"

        fun newInstance(alarm: Alarm, position: Int): SingleAlarmEditorFragment {
            return SingleAlarmEditorFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(KEY_ALARM, alarm)
                bundle.putInt(KEY_POSITION, position)
                arguments = bundle
            }
        }
    }
}