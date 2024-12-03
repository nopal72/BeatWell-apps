package com.example.beatwell.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beatwell.data.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY date DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE date = :date")
    fun getHistoryById(date: String): HistoryEntity

    @Query("SELECT * FROM history ORDER BY date DESC LIMIT 1")
    fun getLastHistory(): HistoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: List<HistoryEntity>)

    @Query("DELETE FROM history")
    fun clearAllData()
}