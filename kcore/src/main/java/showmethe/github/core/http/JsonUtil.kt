package showmethe.github.core.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

import org.json.JSONException
import org.json.JSONObject

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.Collections


object JsonUtil {

    var gson: Gson
    init {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(JsonObject::class.java, JsonDeserializer<Any> { jsonElement, type, jsonDeserializationContext -> jsonElement.asJsonObject })

        gson = builder.disableHtmlEscaping().create()
    }


    fun toJson(dataString : Any?): String? {
        return if (dataString == null) {
            null
        } else gson.toJson(dataString)
    }

    inline fun <reified T> fromJson(content: String): T? {
        if (content.isEmpty()) {
            return null
        }
        try {
            val clazz = T::class.java
            return gson.fromJson(content, clazz)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            return null
        }

    }

    fun <T> fromJson(content: String, token: TypeToken<T>?): T? {
        if (content.isEmpty() || token == null) {
            return null
        }
        try {
            return gson.fromJson<T>(content, token.type)
        } catch (e: JsonSyntaxException) {
            return null
        }

    }


    fun <T> fromJson(content: String, type: Type?): T? {
        if (content.isNotEmpty() && type != null) {
            try {
                return gson.fromJson<T>(content, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
            }

        }
        return null
    }

    fun toMap(obj: Any): Map<String, Any> {
        val element = gson.toJsonTree(obj)
        return gson.fromJson<Map<String ,Any>>(element, Map::class.java)
    }

   inline fun <reified T> fromObject(obj: Any): T {
        val clazz = T::class.java
        val element = gson.toJsonTree(obj)
        return gson.fromJson(element, clazz)
    }

    fun <T> fromObject(obj: Any, token: TypeToken<T>): T? {
        val element = gson.toJsonTree(obj)
        return gson.fromJson<T>(element, token.type)
    }

    fun getMap(map: Map<String, Any>?, key: String?): Map<*, *>? {
        if (map == null || key == null) {
            return null
        }
        val value = map[key]
        return if (value is Map<*, *>) {
            value as Map<*, *>?
        } else null
    }

    fun getLong(map: Map<String, Any>?, key: String?): Long? {
        if (map == null || key == null) {
            return null
        }
        val value = map[key] ?: return null
        if (value is Number) {
            return value.toLong()
        }
        try {
            return java.lang.Long.parseLong(value.toString())
        } catch (e: NumberFormatException) {
            return null
        }

    }

    fun getLongList(map: Map<String, Any>?, key: String?): List<*> {
        if (map == null || key == null) {
            return Collections.EMPTY_LIST as List<*>
        }
        val value = map[key] ?: return Collections.EMPTY_LIST
        if (value is List<*>) {
            val longValues = ArrayList<Long>()
            for (i in value) {
                if (i is Number) {
                    longValues.add(i.toLong())
                }
            }
            return longValues
        }
        return Collections.EMPTY_LIST
    }



    fun findObject(json: String, name: String): Any? {

        var `object`: Any? = null

        if (json.isEmpty() || name.isEmpty()) {
            return null
        }

        try {
            val jsonobject = JSONObject(json)
            if (!jsonobject.has(name)) {
                return null
            } else {
                `object` = jsonobject.get(name)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return `object`
    }
}
