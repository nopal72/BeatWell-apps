package com.example.beatwell.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.data.pref.MessageModel
import com.example.beatwell.data.pref.PredictRequest
import com.example.beatwell.data.pref.UserModel
import com.example.beatwell.data.pref.UserPreference
import com.example.beatwell.data.remote.api.ApiConfig
import com.example.beatwell.data.remote.api.ApiService
import com.example.beatwell.data.remote.response.ActivityResponse
import com.example.beatwell.data.remote.response.ChatbotResponse
import com.example.beatwell.data.remote.response.FoodDetailResponse
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.data.remote.response.HistoryResponse
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.remote.response.NewsResponse
import com.example.beatwell.data.remote.response.PredictResponse
import com.example.beatwell.data.remote.response.RegisterResponse
import com.example.beatwell.data.remote.response.TriviaResponse
import com.example.beatwell.data.room.HistoryDao
import com.example.beatwell.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        CoroutineScope(Dispatchers.IO).launch {
            historyDao.clearAllData()
        }
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
                            userId = loginData.id.toString(),
                            email = loginData.email,
                            name = loginData.name,
                            token = loginData.token,
                            isLogin = true
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

    fun getActivity(): LiveData<Result<ActivityResponse>>{
        val result = MutableLiveData<Result<ActivityResponse>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect{user->
                val client = apiService.getActivity(user.token)
                client.enqueue(object : Callback<ActivityResponse>{
                    override fun onResponse(
                        call: Call<ActivityResponse>,
                        response: Response<ActivityResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            body?.let {
                                result.value = Result.Success(it)
                            }
                        }else{
                            result.value = Result.Error(response.message())
                        }
                    }

                    override fun onFailure(call: Call<ActivityResponse>, t: Throwable) {
                        result.value = Result.Error(t.toString())
                    }
                })
            }
        }

        return result
    }

    fun predict(request: PredictRequest): LiveData<Result<PredictResponse>> {
        val result = MutableLiveData<Result<PredictResponse>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect {user->
                val client = apiService.predict(
                    request,
                    user.token
                )
                client.enqueue(object : Callback<PredictResponse>{
                    override fun onResponse(
                        call: Call<PredictResponse>,
                        response: Response<PredictResponse>
                    ) {
                       if (response.isSuccessful){
                           val body = response.body()
                           body?.let {
                               if (!it.error){
                                   result.postValue(Result.Success(it))
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

    fun chatbot(message: String): LiveData<Result<MessageModel>> {
        val result = MutableLiveData<Result<MessageModel>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect{user->
                val client = apiService.chatBot(message,user.token)
                client.enqueue(object : Callback<ChatbotResponse>{
                    override fun onResponse(
                        call: Call<ChatbotResponse>,
                        response: Response<ChatbotResponse>
                    ) {
                        if (response.isSuccessful){
                            val body = response.body()
                            body?.let {
                                val text = MessageModel(it.data,false)
                                result.value = Result.Success(text)
                            }
                        }
                        else{
                            result.value = Result.Error(response.message())
                        }
                    }

                    override fun onFailure(call: Call<ChatbotResponse>, t: Throwable) {
                        result.value = Result.Error(t.toString())
                    }
                })
            }
        }
        return result
    }

    fun getHistory(): LiveData<Result<HistoryEntity>>{
        val result = MutableLiveData<Result<HistoryEntity>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect {user ->
                val client = apiService.getHistory(
                    user.userId.toInt(),
                    user.token
                )
                client.enqueue(object : Callback<HistoryResponse>{
                    override fun onResponse(
                        call: Call<HistoryResponse>,
                        response: Response<HistoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            body?.let {
                                val historyItems = it.historyItem.map {historyItem ->
                                    HistoryEntity(
                                        date = historyItem.createdAt,
                                        prediction = historyItem.result
                                    )
                                }
                                appExecutors.diskIO.execute {
                                    historyDao.insertHistory(historyItems)
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                        result.value = Result.Error(t.toString())
                    }
                })
                val latestHistory = historyDao.getLastHistory()
                Log.d("UserRepository", "latestHistory: $latestHistory")
                result.postValue(Result.Success(latestHistory))
            }
        }
        return result
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }

    fun getResult(date: String): LiveData<Result<HistoryEntity>> {
        val result = MutableLiveData<Result<HistoryEntity>>()
        result.value = Result.Loading

        appExecutors.diskIO.execute {
            val history = historyDao.getHistoryById(date)
            Log.d("ResultActivity", "prediction: $history")
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
                        }else{
                            result.value = Result.Error(response.message())
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

    fun getFoodDetail(id: Int): LiveData<Result<FoodDetailResponse>> {
        val result = MutableLiveData<Result<FoodDetailResponse>>()
        result.value = Result.Loading
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect { user ->
                val client = apiService.getFoodDetail(id,user.token)
                client.enqueue(object : Callback<FoodDetailResponse>{
                    override fun onResponse(
                        call: Call<FoodDetailResponse>,
                        response: Response<FoodDetailResponse>
                    ) {
                        if (response.isSuccessful){
                            val body = response.body()
                            body?.let {
                                result.value = Result.Success(it)
                            }
                        }else{
                            result.value = Result.Error(response.message())
                        }
                    }

                    override fun onFailure(call: Call<FoodDetailResponse>, t: Throwable) {
                        result.value = Result.Error(t.toString())
                    }
                })
            }
        }

        return result
    }

    fun getNews(): LiveData<Result<NewsResponse>> {
        val result = MutableLiveData<Result<NewsResponse>>()
        result.value = Result.Loading
        val client = ApiConfig.getNewsApiService().getNews()
        client.enqueue(object: Callback<NewsResponse>{
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        result.value = Result.Success(it)
                    }
                }else{
                    result.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                result.value = Result.Error(t.toString())
            }
        })

        return result
    }

    fun setDailyReminder(dailyReminder: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.setDailyReminder(dailyReminder)
        }
    }

    fun getDailyReminder(): Flow<Boolean> = userPreference.getDailyReminder()

    fun getTrivia(): LiveData<Result<TriviaResponse>> {
        val result = MutableLiveData<Result<TriviaResponse>>()
        result.value = Result.Loading

        CoroutineScope(Dispatchers.IO).launch {
            userPreference.getSession().collect {user->
                val client = apiService.getTrivia(user.token)
                client.enqueue(object : Callback<TriviaResponse>{
                    override fun onResponse(
                        call: Call<TriviaResponse>,
                        response: Response<TriviaResponse>
                    ) {
                        if (response.isSuccessful){
                            val body = response.body()
                            body?.let {
                                result.value = Result.Success(body)
                            }
                        }else{
                            result.value = Result.Error(response.message())
                        }
                    }

                    override fun onFailure(call: Call<TriviaResponse>, t: Throwable) {
                        result.value = Result.Error(t.toString())
                    }
                })
            }
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