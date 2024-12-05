package com.example.beatwell.data.pref

data class UserModel (
    val userId: String,
    val email: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
)

data class MessageModel(
    val text: String,
    val isFromUser: Boolean
)

data class PredictRequest(
    val sex: String,
    val age: Int,
    val cigsPerday: Int,
    val BPMeds: Boolean,
    val prevalentStroke: Boolean,
    val prevalentHyp: Boolean,
    val diabetes: Boolean,
    val totChol: Int,
    val sysBP: Int,
    val diaBP: Int,
    val heartRate: Int,
    val glucose: Int,
    val height: Int,
    val weight: Int
)