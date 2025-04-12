package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableStateFlow

@Parcelize
data class GetDurationResponse(

	@field:SerializedName("makan")
	val makan: Float? = null,

	@field:SerializedName("tidur")
	val tidur: Float? = null
) : Parcelable
