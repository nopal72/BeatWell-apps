package com.example.beatwell.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.beatwell.data.pref.UserModel
import com.example.beatwell.data.pref.UserPreference
import com.example.beatwell.data.remote.api.ApiConfig
import com.example.beatwell.data.remote.api.ApiService
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.data.remote.response.HistoryResponse
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.remote.response.RegisterResponse
import com.example.beatwell.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.json.JSONObject
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

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        val result = MutableLiveData<Result<LoginResponse>>()
        result.value = Result.Loading

        val client = apiService.login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("UserRepository", "onResponse: $responseBody")
                    if (!responseBody?.error!!) {
                        Log.d("UserRepository", "onResponse.loginData: ${responseBody.loginData}")
                        val loginData = responseBody.loginData
                        val user = UserModel(
                            loginData.name,
                            loginData.email,
                            loginData.token,
                            true
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            userPreference.saveSession(user)
                        }
                        result.value = Result.Success(responseBody)
                    } else {
                        result.value = Result.Error("Response body is null")
                    }
                } else {
                    result.value = Result.Error("Failed with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        return result
    }

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> {
        val result = MutableLiveData<Result<RegisterResponse>>()
        result.value = Result.Loading
        val client = apiService.register(name,email,password)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ){
                if (response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        if (!it.error){
                            result.value = Result.Success(it)
                        }else{
                            result.value = Result.Error(it.message)
                        }
                    }
                }else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let {
                        JSONObject(it).getString("message")
                    } ?: "Unknown error"
                    result.value = Result.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                result.value = Result.Error(t.toString())
            }

        })
        return result
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

    fun getHistory(): LiveData<Result<HistoryResponse>>{
        val result = MutableLiveData<Result<HistoryResponse>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            val client = apiService.getHistory()
            client.enqueue(object : Callback<HistoryResponse>{
                override fun onResponse(
                    call: Call<HistoryResponse>,
                    response: Response<HistoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            result.value = Result.Success(it)
                        }
                    }
                }
                override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
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