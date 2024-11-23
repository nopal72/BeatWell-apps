package com.example.beatwell.di

import android.content.Context
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.pref.UserPreference
import com.example.beatwell.data.pref.dataStore
import com.example.beatwell.data.remote.api.ApiConfig
import com.example.beatwell.data.room.HistoryDatabase
import com.example.beatwell.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        val database = HistoryDatabase.getInstance(context)
        val dao = database.historyDao()
        return UserRepository.getInstance(pref, apiService, appExecutors, dao)
    }
}