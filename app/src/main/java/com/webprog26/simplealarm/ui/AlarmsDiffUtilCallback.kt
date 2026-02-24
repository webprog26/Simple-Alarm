package com.webprog26.simplealarm.ui

import androidx.recyclerview.widget.DiffUtil
import com.webprog26.simplealarm.data.Alarm

class AlarmsDiffUtilCallback(private val oldList: List<Alarm>,
                             private val newList: List<Alarm>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return newList[newItemPosition].id == oldList[oldItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }
}