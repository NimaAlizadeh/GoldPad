package com.example.goldpad.database.converter

import androidx.room.TypeConverter
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromUser(user: User?): String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun toUser(data: String?): User {
        return gson.fromJson(data, User::class.java)
    }


    @TypeConverter
    fun fromRequestList(requests: List<Request>): String {
        return Gson().toJson(requests) // Convert the list to JSON
    }

    @TypeConverter
    fun toRequestList(json: String): List<Request> {
        val type = object : TypeToken<List<Request>>() {}.type
        return Gson().fromJson(json, type) // Convert JSON back to a list
    }
}
