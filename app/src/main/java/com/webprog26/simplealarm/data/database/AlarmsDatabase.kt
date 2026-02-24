package com.webprog26.simplealarm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.webprog26.simplealarm.data.Alarm

@Database(entities = [Alarm::class], version = 1, exportSchema = false)
abstract class AlarmsDatabase : RoomDatabase() {
    abstract fun alarmsDao(): AlarmsDao
    companion object {
        @Volatile
        private var INSTANCE: AlarmsDatabase? = null
        fun getDatabase(context: Context): AlarmsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmsDatabase::class.java,
                    "alarms_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}