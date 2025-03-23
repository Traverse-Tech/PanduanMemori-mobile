package com.traverse.panduanmemori.data.dataclasses

data class ErrorResponse(
    val responseMessage: String?,
    val responseCode: Int?,
    val responseStatus: String?,
    val errorDescription: String?
)
