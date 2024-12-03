package com.example.beatwell.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "history")
@Parcelize
data class HistoryEntity (

    @PrimaryKey
    @field:ColumnInfo(name = "date")
    val date: String,

    @field:ColumnInfo(name = "prediction")
    val prediction: Int,

): Parcelable