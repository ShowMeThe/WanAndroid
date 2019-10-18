package com.example.database.source.converters

import androidx.databinding.ObservableArrayList
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 14:21
 * Package Name:com.example.database.source.converters
 */
class ArraysConverters {

    @TypeConverter
    fun stringToObject(value: String): ObservableArrayList<String>{
        val listType = object : TypeToken<ObservableArrayList<String>>() {
        }.type
        return  Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: ObservableArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}