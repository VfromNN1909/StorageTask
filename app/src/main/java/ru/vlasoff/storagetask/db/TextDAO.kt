package ru.vlasoff.storagetask.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// тут много лишних запросов, некоторые
@Dao
abstract class TextDAO {

    @Insert
    abstract fun insertText(text: TextEntity)

    /**
     * сначала делал через @Update, чтобы был только один элемент в БД
     * но у меня не работало,
     * так вроде работает
     */

    @Query(
        """
        SELECT * FROM textTable WHERE id = (
            SELECT MAX(id) from textTable
        )
        """
    )
    abstract fun getLastInTable(): TextEntity

    // это чисто для проверки
    @Query("SELECT * FROM textTable")
    abstract fun getAll(): List<TextEntity>

}