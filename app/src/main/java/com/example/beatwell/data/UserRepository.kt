package com.example.beatwell.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.data.pref.PredictRequest
import com.example.beatwell.data.pref.UserModel
import com.example.beatwell.data.pref.UserPreference
import com.example.beatwell.data.remote.api.ApiService
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.data.remote.response.HistoryResponse
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.remote.response.PredictResponse
import com.example.beatwell.data.remote.response.RegisterResponse
import com.example.beatwell.data.room.HistoryDao
import com.example.beatwell.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val appExecutors: AppExecutors,
    private val historyDao: HistoryDao
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
                            saveSession(user)
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
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                result.value = Result.Error(t.toString())
            }

        })
        return result
    }

    fun predict(request: PredictRequest): LiveData<Result<HistoryEntity>> {
        val result = MutableLiveData<Result<HistoryEntity>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect {user->
                val client = apiService.predict(
                    request,
                    user.token
                )
                Log.d("UserRepository", "predict: $request")
                client.enqueue(object : Callback<PredictResponse>{
                    override fun onResponse(
                        call: Call<PredictResponse>,
                        response: Response<PredictResponse>
                    ) {
                       if (response.isSuccessful){
                           val body = response.body()
                           body?.let {
                               if (!it.error){
                                   val currentDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                       Date()
                                   )
                                   val history = HistoryEntity(
                                       date = currentDate,
                                       prediction = it.data,
                                       gender = request.sex,
                                       age = request.age,
                                       cigsPerDay = request.cigsPerday,
                                       bpMeds = request.BPMeds,
                                       prevalentStroke = request.prevalentStroke,
                                       prevalentHyp = request.prevalentHyp,
                                       diabetes = request.diabetes,
                                       totChol = request.totChol,
                                       sysBP = request.sysBP,
                                       diaBP = request.diaBP,
                                       bmi = request.BMI,
                                       heartRate = request.heartRate,
                                       glucose = request.glucose
                                   )
                                   appExecutors.diskIO.execute {
                                       historyDao.insertHistory(history)
                                       val lastHistory = getLastHistory()
                                       result.postValue(Result.Success(lastHistory))
                                   }
                               }else{
                                   result.value = Result.Error(it.message)
                               }
                           }
                       }else{
                           result.value = Result.Error(response.message())
                       }
                    }
                    override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                        result.value = Result.Error(t.toString())
                    }
                })
            }
        }
        return result
    }

    fun getLastHistory(): HistoryEntity{
        return historyDao.getLastHistory()
    }

    fun getResult(id: Int): LiveData<Result<HistoryEntity>> {
        val result = MutableLiveData<Result<HistoryEntity>>()
        result.value = Result.Loading

        appExecutors.diskIO.execute {
            val history = historyDao.getHistoryById(id)
            result.postValue(Result.Success(history))
        }

        return result
    }

    fun getFoods(): LiveData<Result<FoodsResponse>> {
        val result = MutableLiveData<Result<FoodsResponse>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect { it ->
                val client = apiService.getFoods(it.token)
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
            userPreference: UserPreference,
            apiService: ApiService,
            appExecutors: AppExecutors,
            historyDao: HistoryDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(
                    userPreference,
                    apiService,
                    appExecutors,
                    historyDao)
            }.also { instance = it }
    }
}