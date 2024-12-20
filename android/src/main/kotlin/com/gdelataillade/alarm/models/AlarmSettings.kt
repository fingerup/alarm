package com.gdelataillade.alarm.models

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import java.util.Date
import io.flutter.Log
import java.lang.reflect.Type

data class AlarmSettings(
    val id: Int,
    val dateTime: Date,
    val assetAudioPath: String,
    val notificationSettings: NotificationSettings,
    val loopAudio: Boolean,
    val vibrate: Boolean,
    val volume: Double?,
    val volumeEnforced: Boolean = false,
    val fadeDuration: Double,
    val warningNotificationOnKill: Boolean,
    val androidFullScreenIntent: Boolean
) {
    companion object {
        fun fromJson(jsonString: String): AlarmSettings? {
            return try {
                val gson = GsonBuilder()
                    .registerTypeAdapter(Date::class.java, DateDeserializer())
                    .create()
                gson.fromJson(jsonString, AlarmSettings::class.java)
            } catch (e: Exception) {
                Log.e("AlarmSettings", "Error parsing JSON to AlarmSettings: ${e.message}", e)
                null
            }
        }
    }

    fun toJson(): String {
        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateSerializer())
            .create()
        return gson.toJson(this)
    }
}

class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        val dateTimeMicroseconds = json?.asLong ?: 0L
        val dateTimeMilliseconds = dateTimeMicroseconds / 1000
        return Date(dateTimeMilliseconds)
    }
}

class DateSerializer : JsonSerializer<Date> {
    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val dateTimeMicroseconds = src?.time?.times(1000) ?: 0L
        return JsonPrimitive(dateTimeMicroseconds)
    }
}