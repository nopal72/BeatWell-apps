package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class FoodsResponse(

	@field:SerializedName("body")
	val foodsBody: FoodsBody,

	@field:SerializedName("status")
	val status: Int
) : Parcelable

@Parcelize
data class FoodsBody(

	@field:SerializedName("data")
	val data: List<FoodDataItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean
) : Parcelable

@Parcelize
data class FoodDataItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("recipe")
	val recipe: String,

	@field:SerializedName("id")
	val id: String
) : Parcelable
