package com.example.beatwell.data.pref

data class UserModel (
    val email: String,
    val name: String,
    val token: String,
    val isLogin: Boolean
)