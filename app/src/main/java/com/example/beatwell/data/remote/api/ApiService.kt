package com.example.beatwell.data.remote.api

import com.example.beatwell.BuildConfig
import com.example.beatwell.data.pref.EditAccountRequest
import com.example.beatwell.data.pref.PredictRequest
import com.example.beatwell.data.remote.response.ActivityResponse
import com.example.beatwell.data.remote.response.ChatbotResponse
import com.example.beatwell.data.remote.response.DeleteUserResponse
import com.example.beatwell.data.remote.response.EditAccountResponse
import com.example.beatwell.data.remote.response.FoodDetailResponse
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.data.remote.response.HistoryResponse
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.remote.response.NewsResponse
import com.example.beatwell.data.remote.response.PredictResponse
import com.example.beatwell.data.remote.response.RegisterResponse
import com.example.beatwell.data.remote.response.TriviaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/foods")
    fun getFoods(
        @Header("Authorization") token: String
    ): Call<FoodsResponse>

    @GET("/foods/{id}")
    fun getFoodDetail(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<FoodDetailResponse>

    @GET("/users/{id}/histories")
    fun getHistory(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/chatbot")
    fun chatBot(
        @Field("message") message: String,
        @Header("Authorization") token: String
    ): Call<ChatbotResponse>

    @POST("/prediction")
    fun predict(
        @Body request: PredictRequest,
        @Header("Authorization") token: String
    ): Call<PredictResponse>

    @GET("/trivia")
    fun getTrivia(
        @Header("Authorization") token: String
    ): Call<TriviaResponse>

    @GET("/activity")
    fun getActivity(
        @Header("Authorization") token: String
    ): Call<ActivityResponse>

    @GET("v2/top-headlines")
    fun getNews(
        @Query("q") query: String = "health",
        @Query("category") category: String = "health",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Call<NewsResponse>

    @PATCH("/users")
    fun editAccount(
        @Header("Authorization") token: String,
        @Body request: EditAccountRequest
    ): Call<EditAccountResponse>

    @DELETE("/users")
    fun deleteUser(
        @Header("Authorization") token: String
    ): Call<DeleteUserResponse>
}