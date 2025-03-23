package com.traverse.panduanmemori.data.repositories

sealed class ApiState {
    object Idle : ApiState()
    object Loading : ApiState()
    object Success : ApiState()
    data class Error(val message: String, val description: String? = null) : ApiState()
}

const val DEFAULT_ERROR_MESSAGE =  "Terjadi kesalahan"
