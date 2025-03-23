package com.traverse.panduanmemori.data.dataclasses

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	@field:SerializedName("responseMessage")
	val responseMessage: String? = null,

	@field:SerializedName("responseStatus")
	val responseStatus: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("responseCode")
	val responseCode: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("birthdate")
	val birthdate: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("registrationNumber")
	val registrationNumber: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("dementiaStage")
	val dementiaStage: String? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable
