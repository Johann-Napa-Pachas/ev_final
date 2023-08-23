package com.example.ec_final_napapachasjohann.data

import androidx.room.TypeConverter
import com.example.ec_final_napapachasjohann.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromCategory(category: Category): String {
        return Gson().toJson(category)
    }

    @TypeConverter
    fun toCategory(json: String): Category {
        return Gson().fromJson(json, Category::class.java)
    }
}