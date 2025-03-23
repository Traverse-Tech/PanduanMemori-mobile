package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("identifier")
    val identifier: String,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("latitude")
    val latitude: Float,

    @field:SerializedName("longitude")
    val longitude: Float,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("birthdate")
    val birthdate: String,

    @field:SerializedName("gender")
    val gender: String,
)
