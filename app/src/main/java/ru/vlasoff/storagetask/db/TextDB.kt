package ru.vlasoff.storagetask.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TextEntity::class], version = 1)
abstract class TextDB : RoomDatabase() {
    abstract fun getTextDAO(): TextDAO
}