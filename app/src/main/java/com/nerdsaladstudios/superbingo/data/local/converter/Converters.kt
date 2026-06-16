package com.nerdsaladstudios.superbingo.data.local.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromLongList(list: List<Long>): String = list.joinToString(",")

    @TypeConverter
    fun toLongList(value: String): List<Long> = 
        if (value.isBlank()) emptyList() else value.split(",").mapNotNull { it.toLongOrNull() }
}
