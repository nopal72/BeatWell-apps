package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("data")
	val data: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean
) : Parcelable
