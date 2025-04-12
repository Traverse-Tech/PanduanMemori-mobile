package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class GetCaregiverPatientsResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("responseMessage")
	val responseMessage: String? = null,

	@field:SerializedName("responseStatus")
	val responseStatus: String? = null,

	@field:SerializedName("responseCode")
	val responseCode: Int? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("registrationNumber")
	val registrationNumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("dementiaStage")
	val dementiaStage: String? = null,

	@field:SerializedName("age")
	val age: Int? = null
) : Parcelable
