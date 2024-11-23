package com.example.beatwell.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beatwell.data.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id = :id")
    fun getHistoryById(id: Int): LiveData<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(history: HistoryEntity)

    @Delete
    fun deleteHistory(history: HistoryEntity)
}