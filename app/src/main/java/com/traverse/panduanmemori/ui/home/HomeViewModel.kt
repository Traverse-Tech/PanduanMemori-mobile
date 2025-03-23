package com.traverse.panduanmemori.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.traverse.panduanmemori.data.contexts.IsLoginContext
import com.traverse.panduanmemori.data.contexts.UserContext
import com.traverse.panduanmemori.data.contexts.isLoginDataStore
import com.traverse.panduanmemori.data.contexts.sessionDataStore
import com.traverse.panduanmemori.data.dataclasses.ErrorResponse
import com.traverse.panduanmemori.data.repositories.ApiConfig
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.data.repositories.DEFAULT_ERROR_MESSAGE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.File

class HomeViewModel(context: Context) : ViewModel() {
    private val apiConfig = ApiConfig(context)
    private val userContext = UserContext.getInstance(context.sessionDataStore)
    private val isLoginContext = IsLoginContext.getInstance(context.isLoginDataStore)

    suspend fun isShowWelcomeToast(): Boolean {
        return isLoginContext.getLoginState().first()
    }

    suspend fun resetLoginContext() {
        isLoginContext.setLoginState(false)
    }

    private val _buddyState = MutableStateFlow<ApiState>(ApiState.Idle)
    val buddyState: StateFlow<ApiState> = _buddyState

    fun setBuddyState(buddyState: ApiState) {
        _buddyState.value = buddyState
    }

    private val _buddyConversationResponse = MutableLiveData("")
    val buddyConversationResponse: LiveData<String> = _buddyConversationResponse

    suspend fun buddyConversation(audioFile: File) {
        _buddyState.value = ApiState.Loading

        try {
            delay(3000)
            _buddyState.value = ApiState.Success
            _buddyConversationResponse.value = "Apakah jawaban Nana sudah cukup membantu?"
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            _buddyState.value = ApiState.Error(
                message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                description = errorResponse.errorDescription
            )
        } catch (e: Exception) {
            _buddyState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
        }
    }
}
