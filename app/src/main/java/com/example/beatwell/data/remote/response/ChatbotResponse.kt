package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ChatbotResponse(

	@field:SerializedName("response")
	val response: String,

	@field:SerializedName("error")
	val error: Boolean

) : Parcelable
