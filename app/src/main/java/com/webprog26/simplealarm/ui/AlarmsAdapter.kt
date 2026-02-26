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
import com.webprog26.simplealarm.getTimeString

class AlarmsAdapter(
    private val mAlarmsList: MutableList<Alarm>,
    private val onAlarmClickListener: OnAlarmClickListener,
    private val onAlarmStateUpdatedListener: OnAlarmStateUpdatedListener,
    private val onAlarmTimeClickListener: OnAlarmTimeClickListener,
) :
    RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {

    interface OnAlarmClickListener {
        fun onAlarmClick(alarm: Alarm)
    }

     interface OnAlarmStateUpdatedListener {
         fun onAlarmStateUpdated(alarm: Alarm)
     }

    interface OnAlarmTimeClickListener {
        fun onAlarmTimeClick(alarm: Alarm)
    }

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
        val alarm = mAlarmsList[position]
        holder.bind(alarm)
        holder.view.setOnClickListener { view ->
            onAlarmClickListener.onAlarmClick(alarm)
        }

        holder.view.findViewById<SwitchCompat>(R.id.sw_is_alarm_active).setOnCheckedChangeListener { _, isChecked ->
            onAlarmStateUpdatedListener.onAlarmStateUpdated(alarm.copy(isActive = isChecked))
        }

        holder.view.findViewById<TextView>(R.id.tv_alarm_time).setOnClickListener { v ->
            onAlarmTimeClickListener.onAlarmTimeClick(alarm)
        }

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

    inner class AlarmViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bind(alarm: Alarm) {
            view.findViewById<TextView>(R.id.tv_alarm_time)
                .text = alarm.getTimeString()

                view.findViewById<SwitchCompat>(R.id.sw_is_alarm_active).isChecked = alarm.isActive

                val tvAlarmScheduledTo = view.findViewById<TextView>(R.id.tv_alarm_scheduled_to)
                val context = view.context

                if (alarm.alarmDaysSelectedIds.isEmpty()) {
                    tvAlarmScheduledTo.text =
                        context.getString(R.string.alarm_not_scheduled_text)
                } else if (alarm.alarmDaysSelectedIds.size == NUM_OF_DAYS_IN_WEEK){
                    tvAlarmScheduledTo.text =
                        context.getString(R.string.alarm_scheduled_to_every_day)
                } else {
                    tvAlarmScheduledTo.text = alarm.alarmDaysSelectedNames
                }

                view.findViewById<TextView>(R.id.tv_alarm_name).text = alarm.alarmName
        }
    }



    companion object {
        const val NUM_OF_DAYS_IN_WEEK = 7
    }
}