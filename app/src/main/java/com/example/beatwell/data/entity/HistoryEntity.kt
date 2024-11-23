package com.example.beatwell.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "history")
@Parcelize
class HistoryEntity (
    @field:ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "date")
    val date: String,

    @field:ColumnInfo(name = "prediction")
    val prediction: Int,

    @field:ColumnInfo(name = "gender")
    val gender: String,

    @field:ColumnInfo(name = "age")
    val age: Int,

    @field:ColumnInfo(name = "cigsPerDay")
    val cigsPerDay: Int,

    @field:ColumnInfo(name = "bpMeds")
    val bpMeds: Boolean,

    @field:ColumnInfo(name = "prevalentStroke")
    val prevalentStroke: Boolean,

    @field:ColumnInfo(name = "prevalentHyp")
    val prevalentHyp: Boolean,

    @field:ColumnInfo(name = "diabetes")
    val diabetes: Boolean,

    @field:ColumnInfo(name = "totChol")
    val totChol: Int,

    @field:ColumnInfo(name = "sysBP")
    val sysBP: Int,

    @field:ColumnInfo(name = "diaBP")
    val diaBP: Int,

    @field:ColumnInfo(name = "bmi")
    val bmi: Int,

    @field:ColumnInfo(name = "heartRate")
    val heartRate: Int,

    @field:ColumnInfo(name = "glucose")
    val glucose: Int

): Parcelable