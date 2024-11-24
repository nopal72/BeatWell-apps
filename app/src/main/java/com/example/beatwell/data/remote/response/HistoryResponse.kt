package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HistoryResponse(

	@field:SerializedName("data")
	val historyItem: List<HistoryItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean
) : Parcelable

@Parcelize
data class HistoryItem(

	@field:SerializedName("result")
	val result: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: Int
) : Parcelable
