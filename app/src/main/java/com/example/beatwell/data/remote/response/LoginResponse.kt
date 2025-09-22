package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	@SerializedName("message")
	val message: String,

	val token: String,

	@SerializedName("error")
	val error: Boolean,

	@SerializedName("user")
	val userData: UserData
) : Parcelable

@Parcelize
data class UserData(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String

) : Parcelable
