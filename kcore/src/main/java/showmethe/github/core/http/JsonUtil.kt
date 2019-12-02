package showmethe.github.core.http

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

import org.json.JSONException
import org.json.JSONObject
import showmethe.github.core.util.match.isNotNull

import java.lang.reflect.Type
import java.util.ArrayList
import java.util.Collections


val gson: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    val builder = GsonBuilder()
    builder.registerTypeAdapter(
        JsonObject::class.java,
        JsonDeserializer<Any> { jsonElement, type, jsonDeserializationContext -> jsonElement.asJsonObject })
    builder.disableHtmlEscaping().create()
}

inline fun <reified T> String?.fromJson(): T? {
    if (isNullOrEmpty()) {
        return null
    }
    return try {
        val clazz = T::class.java
        gson.fromJson(this, clazz)
    } catch (e: JsonSyntaxException) {
        e.printStackTrace()
        null
    }
}

fun Any.toJson(): String? {
    return if (isNotNull()) {
        null
    } else gson.toJson(this)
}

inline fun <reified T> Any.fromObject(): T {
    val clazz = T::class.java
    val element = gson.toJsonTree(this)
    return gson.fromJson(element, clazz)
}

inline fun <reified T> String?.fromJson(token: TypeToken<T>): T? {
    if (this.isNullOrEmpty()) {
        return null
    }
    return try {
        gson.fromJson<T>(this, token.type)
    } catch (e: JsonSyntaxException) {
        null
    }
}

fun Any.toMap(): Map<String, Any> {
    val element = gson.toJsonTree(this)
    return gson.fromJson<Map<String, Any>>(element, Map::class.java)
}

