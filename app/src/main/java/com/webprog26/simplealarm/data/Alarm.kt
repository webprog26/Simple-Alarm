package com.webprog26.simplealarm.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "alarm_hour") var hour: Int,
    @ColumnInfo(name = "alarm_minute") var minute: Int,
    @ColumnInfo(name = "alarm_is_active") var isActive: Boolean
) : Parcelable