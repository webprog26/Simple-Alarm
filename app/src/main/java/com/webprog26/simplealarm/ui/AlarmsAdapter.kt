package com.webprog26.simplealarm.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.webprog26.simplealarm.R
import com.webprog26.simplealarm.data.Alarm

class AlarmsAdapter(private val mAlarmsList: MutableList<Alarm>) :
    RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_single_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AlarmViewHolder,
        position: Int
    ) {
        holder.bind(mAlarmsList[position])
    }

    override fun getItemCount(): Int {
        return mAlarmsList.size
    }

    fun updateAlarmsData(alarms: List<Alarm>) {
        val alarmsDiffUtilCallback = AlarmsDiffUtilCallback(mAlarmsList, alarms)
        val diffResult = DiffUtil.calculateDiff(alarmsDiffUtilCallback)
        mAlarmsList.clear()
        mAlarmsList.addAll(alarms)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class AlarmViewHolder( var view: View)
        : RecyclerView.ViewHolder(view) {

        fun bind(alarm: Alarm) {
            view.findViewById<TextView>(R.id.tv_alarm_time)
                .text = buildString {
                append(alarm.hour.adjustTimeValue())
                append(" : ")
                append(alarm.minute.adjustTimeValue())
            }
            view.findViewById<SwitchCompat>(R.id.sw_is_alarm_active)
                .isChecked = alarm.isActive
        }

        private fun Int.adjustTimeValue(): String {
            return if (this >= 0 && this < 10) {
                "0${this}"
            } else {
                this.toString()
            }
        }
    }
}