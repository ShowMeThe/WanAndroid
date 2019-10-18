package showmethe.github.core.util.rden

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.rden
 */

class Converters {

    @TypeConverter
    fun stringToObject(value: String): ArrayList<*>{
        if(value.isEmpty()){
            return ArrayList<String>()
        }else{
            val listType = object : TypeToken<ArrayList<*>>() {
            }.type
            return  Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun objectToString(list: ArrayList<*>?): String {
        if(list.isNullOrEmpty()){
            return "[]"
        }else{
            val gson = Gson()
            return gson.toJson(list)
        }
    }

}