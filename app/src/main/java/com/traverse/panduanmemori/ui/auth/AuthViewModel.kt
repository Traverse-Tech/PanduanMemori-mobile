package com.traverse.panduanmemori.ui.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.traverse.panduanmemori.data.contexts.IsLoginContext
import com.traverse.panduanmemori.data.contexts.UserContext
import com.traverse.panduanmemori.data.contexts.isLoginDataStore
import com.traverse.panduanmemori.data.contexts.sessionDataStore
import com.traverse.panduanmemori.data.dataclasses.ErrorResponse
import com.traverse.panduanmemori.data.dataclasses.LoginRequest
import com.traverse.panduanmemori.data.dataclasses.OnboardingItem
import com.traverse.panduanmemori.data.models.Gender
import com.traverse.panduanmemori.data.models.User
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.data.repositories.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(context: Context): ViewModel() {
    private val apiConfig = ApiConfig(context)
    private val userContext = UserContext.getInstance(context.sessionDataStore)
    private val isLoginContext = IsLoginContext.getInstance(context.isLoginDataStore)

    // AUTH
    private val _userRole = MutableLiveData<UserRole>()

    fun setUserRole(role: UserRole) {
        _userRole.value = role
    }

    fun getUserRole(): UserRole? {
        return _userRole.value
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                val response = apiConfig.getAuthApiService().login(LoginRequest(identifier, password))
                val user = User(
                    token = response.token ?: "",
                    name = response.user?.name ?: "",
                    phoneNumber = response.user?.phoneNumber ?: "",
                    email = response.user?.email ?: "",
                    registrationNumber = response.user?.registrationNumber ?: "",
                    role = response.user?.role?.let { UserRole.valueOf(it) } ?: UserRole.CAREGIVER,
                    address = response.user?.address ?: "",
                    birthdate = response.user?.birthdate ?: "",
                    gender = response.user?.gender?.let { Gender.valueOf(it) } ?: Gender.MAN,
                    dementiaStage = response.user?.dementiaStage
                )
                userContext.saveSession(user)
                isLoginContext.setLoginState(true)
                _loginState.value = LoginState.Success
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _loginState.value = LoginState.Error(
                    message = errorResponse.responseMessage ?: "Terjadi kesalahan",
                    description = errorResponse.errorDescription
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Terjadi kesalahan", e.message)
            }
        }
    }

    suspend fun logout() {
        userContext.logout()
    }

    // SPLASH SCREEN
    private val _isAuthenticated = MutableLiveData(false)
    val isAuthenticated: LiveData<Boolean> get() = _isAuthenticated

    fun checkAuthentication() {
        viewModelScope.launch {
            userContext.getSession()
                .collect { user ->
                    _isAuthenticated.value = user.token.isNotEmpty()
                }
        }
    }


    // ONBOARDING
    private val _onboardingItems = MutableStateFlow(
        listOf(
            OnboardingItem(
                imageUrl = "preview_map.png",
                title = "AI Conversation Buddy",
                description = "Lacak keberadaan pasien untuk memastikan keselamatan mereka"
            ),
            OnboardingItem(
                imageUrl = "preview_buddy.png",
                title = "AI Conversation Buddy",
                description = "Teman yang selalu siap membantu dan menjawab pertanyaanmu!"
            ),
            OnboardingItem(
                imageUrl = "preview_dashboard.png",
                title = "Pantau Aktivitas Pasien",
                description = "Dapatkan rangkuman per pekan beserta informasi lengkap terkait aktivitas pasien"
            )
        )
    )

    val onboardingItems: StateFlow<List<OnboardingItem>> = _onboardingItems

    private val _onboardingCurrentIndex = mutableStateOf(0)
    val onboardingCurrentIndex: State<Int> = _onboardingCurrentIndex

    fun nextItem() {
        if (_onboardingCurrentIndex.value < _onboardingItems.value.size - 1) {
            _onboardingCurrentIndex.value += 1
        }
    }

    fun resetItem() {
        _onboardingCurrentIndex.value = 0
    }
}