package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("risk_score")
	val riskScore: Float,

	@field:SerializedName("risk_label")
	val risk_label: String

) : Parcelable


