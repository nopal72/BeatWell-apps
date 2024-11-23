package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("data")
	val predictData: PredictData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("exp")
	val exp: Int,

	@field:SerializedName("iat")
	val iat: Int,

	@field:SerializedName("email")
	val email: String
) : Parcelable

@Parcelize
data class PredictData(

	@field:SerializedName("persentage")
	val persentage: Int,

	@field:SerializedName("user")
	val user: User
) : Parcelable
