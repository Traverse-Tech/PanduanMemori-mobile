package com.traverse.panduanmemori.ui.auth

const val ONBOARDING_SCREEN = "onboarding_screen"
const val ROLE_SELECTION_SCREEN = "role_selection_screen"
const val LOGIN_SCREEN = "login_screen"
const val PATIENT_PROFILE_SCREEN = "patient_profile_screen"
const val REGISTER_SCREEN = "register_screen"

sealed class AuthenticatedState {
    object Unauthenticated : AuthenticatedState()
    object Authenticated : AuthenticatedState()
    object Unassigned : AuthenticatedState()
}

data class SelectedPatient(
    val id: String,
    val name: String,
    val age: Int
)
