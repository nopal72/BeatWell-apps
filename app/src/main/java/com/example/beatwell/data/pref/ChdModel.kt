package com.example.beatwell.data.pref

data class ChdRequest(
    val male: Int,
    val age: Int,
    val currentSmoker: Int,
    val cigsPerDay: Int,
    val BPMeds: Int,
    val prevalentStroke: Int,
    val prevalentHyp: Int,
    val diabetes: Int,
    val totChol: Double,
    val sysBP: Double,
    val diaBP: Double,
    val BMI: Double,
    val heartRate: Double,
    val glucose: Double
)