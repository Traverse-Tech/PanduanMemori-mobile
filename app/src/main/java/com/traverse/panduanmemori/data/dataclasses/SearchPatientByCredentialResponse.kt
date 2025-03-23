package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SearchPatientByCredentialResponse(

	@field:SerializedName("patientId")
	val patientId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("responseMessage")
	val responseMessage: String? = null,

	@field:SerializedName("responseStatus")
	val responseStatus: String? = null,

	@field:SerializedName("responseCode")
	val responseCode: Int? = null
) : Parcelable
