package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ExerciseResponse(
	val exerciseBody: ExerciseBody,
	val status: Int
) : Parcelable

@Parcelize
data class ExerciseBody(
	val exerciseData: ExerciseData,
	val message: String,
	val error: Boolean
) : Parcelable

@Parcelize
data class ExerciseData(
	val name: String,
	val id: String,
	val detail: String
) : Parcelable
