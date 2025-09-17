package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	@SerializedName("message")
	val message: String,

	val token: String

//	@SerializedName("error")
//	val error: Boolean,
//
//	@SerializedName("data")
//	val loginData: LoginData
) : Parcelable

@Parcelize
data class LoginData(

	@field:SerializedName("token")
	val token: String
) : Parcelable
