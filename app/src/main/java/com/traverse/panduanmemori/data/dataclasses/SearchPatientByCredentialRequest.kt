package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName

data class SearchPatientByCredentialRequest (
    @field:SerializedName("identifier")
    val identifier: String,

    @field:SerializedName("password")
    val password: String
)
