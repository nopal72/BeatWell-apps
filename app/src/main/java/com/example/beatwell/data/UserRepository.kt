package com.example.beatwell.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.beatwell.data.pref.UserModel
import com.example.beatwell.data.pref.UserPreference
import com.example.beatwell.data.remote.api.ApiConfig
import com.example.beatwell.data.remote.api.ApiService
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val userPreference: UserPreference, private val apiService: ApiService, private val appExecutors: AppExecutors
){

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

    fun getFoods(): LiveData<Result<FoodsResponse>> {
        val result = MutableLiveData<Result<FoodsResponse>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val client = apiService.getFoods()
            client.enqueue(object : Callback<FoodsResponse> {
                override fun onResponse(
                    call: Call<FoodsResponse>,
                    response: Response<FoodsResponse>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            result.value = Result.Success(it)
                        }
                    }
                }

                override fun onFailure(call: Call<FoodsResponse>, t: Throwable) {
                    result.value = Result.Error(t.toString())
                }
            })
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, ApiConfig.getApiService(), AppExecutors())
            }.also { instance = it }
    }
}