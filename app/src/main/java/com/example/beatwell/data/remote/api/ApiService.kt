package com.example.beatwell.data.remote.api

import com.example.beatwell.data.pref.PredictRequest
import com.example.beatwell.data.remote.response.FoodDetailResponse
import com.example.beatwell.data.remote.response.FoodsResponse
import com.example.beatwell.data.remote.response.HistoryResponse
import com.example.beatwell.data.remote.response.LoginResponse
import com.example.beatwell.data.remote.response.NewsResponse
import com.example.beatwell.data.remote.response.PredictResponse
import com.example.beatwell.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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

    @POST("/prediction")
    fun predict(
        @Body request: PredictRequest,
        @Header("Authorization") token: String
    ): Call<PredictResponse>

    @GET("v2/top-headlines")
    fun getNews(
        @Query("q") query: String = "chd",
        @Query("apiKey") apiKey: String = "8b5c302a4ca54e198d88dc1d18b9c37b"
    ): Call<NewsResponse>
}