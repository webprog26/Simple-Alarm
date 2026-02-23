package com.webprog26.simplealarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _mButtonClicked = MutableLiveData<Boolean>();
    val mButtonClicked: LiveData<Boolean>
        get() = _mButtonClicked

    fun onAddAlarmButtonClick() {
        this._mButtonClicked.value = true
    }
}