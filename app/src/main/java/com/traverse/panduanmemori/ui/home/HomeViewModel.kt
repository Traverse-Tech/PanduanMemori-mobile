package com.traverse.panduanmemori.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.traverse.panduanmemori.data.contexts.IsLoginContext
import com.traverse.panduanmemori.data.contexts.UserContext
import com.traverse.panduanmemori.data.contexts.isLoginDataStore
import com.traverse.panduanmemori.data.contexts.sessionDataStore
import com.traverse.panduanmemori.data.dataclasses.ErrorResponse
import com.traverse.panduanmemori.data.dataclasses.GetDurationResponse
import com.traverse.panduanmemori.data.dataclasses.GetKesesuaianJadwalResponse
import com.traverse.panduanmemori.data.repositories.ApiConfig
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.data.repositories.DEFAULT_ERROR_MESSAGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class HomeViewModel(context: Context) : ViewModel() {
    private val apiConfig = ApiConfig(context)
    private val apiAiConfig = ApiConfig(context, true)
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

    data class BuddyConversationResponse(
        var audioPath: String,
        var transcript: String
    )

    private val _buddyConversationResponse = MutableStateFlow<BuddyConversationResponse>(
        BuddyConversationResponse(audioPath = "", transcript = "")
    )
    val buddyConversationResponse: StateFlow<BuddyConversationResponse> = _buddyConversationResponse

    suspend fun buddyConversation(audioFile: File) {
        _buddyState.value = ApiState.Loading

        try {
            val requestBody = RequestBody.create(
                "audio/wav".toMediaTypeOrNull(),
                audioFile
            )
            val audioPart = MultipartBody.Part.createFormData("audio", audioFile.name, requestBody);
            val response = apiConfig.getPatientApiService().buddyConversation(audioPart);
            _buddyState.value = ApiState.Success
            _buddyConversationResponse.value.audioPath = response.audioResponseUrl.toString()
            _buddyConversationResponse.value.transcript = response.aiResponse.toString()
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

    fun setBuddyFollowUpText() {
        _buddyConversationResponse.value.transcript = "Apakah jawaban Nana sudah cukup membantu?"
    }

    private val _dashboardApiState = MutableStateFlow<ApiState>(ApiState.Idle)
    val dashboardApiState: StateFlow<ApiState> get() = _dashboardApiState

    private val _dashboardDurationData = MutableStateFlow<GetDurationResponse>(GetDurationResponse(makan = 0f, tidur = 0f))
    val dashboardDurationData: StateFlow<GetDurationResponse> get() = _dashboardDurationData

    private val _dashboardKesesuaianJadwalData = MutableStateFlow<GetKesesuaianJadwalResponse>(GetKesesuaianJadwalResponse())
    val dashboardKesesuaianJadwalData: StateFlow<GetKesesuaianJadwalResponse> get() = _dashboardKesesuaianJadwalData

    suspend fun getDashboardDataPage() {
        _dashboardApiState.value = ApiState.Loading

        try {
            _dashboardDurationData.value = apiAiConfig.getDashboardApiService().getDuration()
            _dashboardKesesuaianJadwalData.value = apiAiConfig.getDashboardApiService().getKesesuaianJadwal()
            _dashboardApiState.value = ApiState.Success
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            _dashboardApiState.value = ApiState.Error(
                message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                description = errorResponse.errorDescription
            )
        } catch (e: Exception) {
            _dashboardApiState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
        }
    }
}
