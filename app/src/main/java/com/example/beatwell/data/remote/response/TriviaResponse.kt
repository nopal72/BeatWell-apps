package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class TriviaResponse(

	@field:SerializedName("data")
	val triviaData: TriviaData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean
) : Parcelable

@Parcelize
data class TriviaData(

	@field:SerializedName("trivia")
	val trivia: String
) : Parcelable
