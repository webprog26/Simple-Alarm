package com.webprog26.simplealarm.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webprog26.simplealarm.data.database.AlarmsDao
import kotlinx.coroutines.launch

class AlarmsViewModel(private val alarmsDao: AlarmsDao) : ViewModel() {
    val allAlarms: LiveData<List<Alarm>> = alarmsDao.getAllAlarms()

    fun insert(alarm: Alarm) {
        viewModelScope.launch {
            alarmsDao.insert(alarm)
        }
    }
    fun update(alarm: Alarm) {
        viewModelScope.launch {
            alarmsDao.update(alarm)
        }
    }
    fun delete(alarm: Alarm) {
        viewModelScope.launch {
            alarmsDao.delete(alarm)
        }
    }
}