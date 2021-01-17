package ru.vlasoff.storagetask.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textTable")
data class TextEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
)