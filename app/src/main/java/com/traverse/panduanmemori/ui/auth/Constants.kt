package com.traverse.panduanmemori.ui.auth

const val ONBOARDING_SCREEN = "onboarding_screen"
const val ROLE_SELECTION_SCREEN = "role_selection_screen"
const val LOGIN_SCREEN = "login_screen"
const val REGISTER_SCREEN = "register_screen"

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String, val description: String? = null) : LoginState()
}
