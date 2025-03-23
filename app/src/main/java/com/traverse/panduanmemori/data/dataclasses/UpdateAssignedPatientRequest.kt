package com.traverse.panduanmemori.data.dataclasses

import com.google.gson.annotations.SerializedName

data class UpdateAssignedPatientRequest(
    @field:SerializedName("patientId")
    val patientId: String
)
