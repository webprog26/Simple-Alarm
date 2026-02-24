package com.webprog26.simplealarm.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.webprog26.simplealarm.data.Alarm

@Dao
interface AlarmsDao {

    @Insert
    suspend fun insert(alarm: Alarm)
    @Update
    suspend fun update(alarm: Alarm)
    @Delete
    suspend fun delete(alarm: Alarm)
    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<Alarm>>
}