package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FoodDetailResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("ingredient")
	val ingredient: List<String>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("recipe")
	val recipe: List<String>,

	@field:SerializedName("id")
	val id: Int
) : Parcelable
