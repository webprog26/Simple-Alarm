package com.webprog26.simplealarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.webprog26.simplealarm.data.Alarm

class MainViewModel : ViewModel() {

    private val _mButtonClicked = MutableLiveData<Boolean>();
    val mButtonClicked: LiveData<Boolean>
        get() = _mButtonClicked

    private val _mAlarmWithPositionClicked = MutableLiveData<Pair<Alarm, Int>>()
    val mAlarmWithPositionClicked: LiveData<Pair<Alarm, Int>>
        get() = _mAlarmWithPositionClicked

    private val _mAlarmWithPositionUpdated = MutableLiveData<Pair<Alarm, Int>>()
    val mAlarmWithPositionUpdated: LiveData<Pair<Alarm, Int>>
        get() = _mAlarmWithPositionUpdated

    private val _mAlarmDeleted = MutableLiveData<Alarm>()
    val mAlarmDeleted: LiveData<Alarm>
        get() = _mAlarmDeleted

    fun onAddAlarmButtonClick() {
        this._mButtonClicked.value = true
    }

    fun onAlarmClick(alarm: Alarm, position: Int) {
        this._mAlarmWithPositionClicked.value = Pair<Alarm, Int>(alarm, position)
    }

    fun onAlarmUpdated(alarm: Alarm, position: Int) {
        this._mAlarmWithPositionUpdated.value = Pair<Alarm, Int>(alarm, position)
    }

    fun onAlarmDeleted(alarm: Alarm) {
        this._mAlarmDeleted.value = alarm
    }

}