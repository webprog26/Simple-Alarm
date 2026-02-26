package com.webprog26.simplealarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.webprog26.simplealarm.data.Alarm
import com.webprog26.simplealarm.ui.AlarmsAdapter

class MainFragment : Fragment() {

    private lateinit var alarmsAdapter: AlarmsAdapter

    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FloatingActionButton>(R.id.fab_add_alarm).setOnClickListener { view ->
            mainViewModel.onAddAlarmButtonClick()
        }

        val alarmsRecyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.rv_alarms_list)
        alarmsRecyclerView.layoutManager = LinearLayoutManager(context)

        this.alarmsAdapter = AlarmsAdapter(mutableListOf(), object : AlarmsAdapter.OnAlarmClickListener {
            override fun onAlarmClick(
                alarm: Alarm,
                position: Int
            ) {
               mainViewModel.onAlarmClick(alarm, position)
            }
        }, object : AlarmsAdapter.OnAlarmStateUpdatedListener {
            override fun onAlarmStateUpdated(alarm: Alarm, position: Int) {
                mainViewModel.onAlarmUpdated(alarm, position)
            }
        },
            object : AlarmsAdapter.OnAlarmTimeClickListener {
                override fun onAlarmTimeClick(
                    alarm: Alarm
                ) {
                    mainViewModel.onAlarmTimeClick(alarm)
                }
            })

        alarmsRecyclerView.adapter = alarmsAdapter
    }

    fun updateAlarmsList(alarms: List<Alarm>) {
        alarmsAdapter.updateAlarmsData(alarms)
    }
}