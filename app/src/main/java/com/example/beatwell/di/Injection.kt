package com.example.beatwell.di

import android.content.Context
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.pref.UserPreference
import com.example.beatwell.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}