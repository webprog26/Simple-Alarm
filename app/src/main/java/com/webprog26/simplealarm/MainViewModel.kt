package com.webprog26.simplealarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.webprog26.simplealarm.data.Alarm

class MainViewModel : ViewModel() {

    private val _mButtonClicked = MutableLiveData<Boolean>();
    val mButtonClicked: LiveData<Boolean>
        get() = _mButtonClicked

    private val _mAlarmWithPositionClicked = MutableLiveData<Alarm>()
    val mAlarmWithPositionClicked: LiveData<Alarm>
        get() = _mAlarmWithPositionClicked

    private val _mAlarmWithPositionUpdated = MutableLiveData<Alarm>()
    val mAlarmWithPositionUpdated: LiveData<Alarm>
        get() = _mAlarmWithPositionUpdated

    private val _mAlarmDeleted = MutableLiveData<Alarm>()
    val mAlarmDeleted: LiveData<Alarm>
        get() = _mAlarmDeleted

    private val _mAlarmTimeClicked = MutableLiveData<Alarm>()
    val mAlarmTimeClicked: LiveData<Alarm>
        get() = _mAlarmTimeClicked

    fun onAddAlarmButtonClick() {
        this._mButtonClicked.value = true
    }

    fun onAlarmClick(alarm: Alarm) {
        this._mAlarmWithPositionClicked.value = alarm
    }

    fun onAlarmUpdated(alarm: Alarm) {
        this._mAlarmWithPositionUpdated.value = alarm
    }

    fun onAlarmDeleted(alarm: Alarm) {
        this._mAlarmDeleted.value = alarm
    }

    fun onAlarmTimeClick(alarm: Alarm) {
        this._mAlarmTimeClicked.value = alarm
    }

}