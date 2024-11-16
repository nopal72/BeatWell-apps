package com.example.beatwell.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class HistoryResponse(
	val historyBody: HistoryBody,
	val status: Int
) : Parcelable

@Parcelize
data class HistoryDataItem(
	val lastChecked: String,
	val id: String,
	val status: String
) : Parcelable

@Parcelize
data class HistoryBody(
	val data: List<HistoryDataItem>,
	val message: String,
	val error: Boolean
) : Parcelable
