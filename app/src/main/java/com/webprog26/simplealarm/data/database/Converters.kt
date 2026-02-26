package com.webprog26.simplealarm.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromIntList(value: List<Int>?) : String? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, type)
    }
}