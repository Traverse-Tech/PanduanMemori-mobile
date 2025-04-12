package com.traverse.panduanmemori.data.models

data class User(
    val token: String,
    val name: String,
    val phoneNumber: String,
    val email: String?,
    val registrationNumber: String?,
    val role: UserRole,
    val address: String,
    val birthdate: String,
    val gender: Gender,
    val dementiaStage: String?,
    val isAssignedToPatient: Boolean?
)

enum class UserRole {
    PATIENT,
    CAREGIVER
}

enum class Gender {
    MAN,
    WOMAN
}

enum class DementiaStage {
    EARLY,
    MIDDLE,
    LATE
}
