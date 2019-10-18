package com.example.database.source.converters

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
    fun stringToObject(value: String): ArrayList<String>{
        val listType = object : TypeToken<ArrayList<String>>() {
        }.type
        return  Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

}