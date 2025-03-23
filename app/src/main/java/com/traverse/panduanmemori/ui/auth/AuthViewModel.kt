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
import com.traverse.panduanmemori.data.dataclasses.*
import com.traverse.panduanmemori.data.models.Gender
import com.traverse.panduanmemori.data.models.User
import com.traverse.panduanmemori.data.models.UserRole
import com.traverse.panduanmemori.data.repositories.ApiConfig
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.data.repositories.DEFAULT_ERROR_MESSAGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

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

    private val _loginState = MutableStateFlow<ApiState>(ApiState.Idle)
    val loginState: StateFlow<ApiState> = _loginState

    fun setLoginState(loginState: ApiState) {
        _loginState.value = loginState
    }

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ApiState.Loading

            try {
                val response = apiConfig.getAuthApiService().login(LoginRequest(identifier, password, _userRole.value.toString()))
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
                    dementiaStage = response.user?.dementiaStage,
                    patientId = response.user?.patientId ?: ""
                )
                userContext.saveSession(user)
                checkAuthentication()
                isLoginContext.setLoginState(true)
                _loginState.value = ApiState.Success
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _loginState.value = ApiState.Error(
                    message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                    description = errorResponse.errorDescription
                )
            } catch (e: Exception) {
                _loginState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    private val _registerState = MutableStateFlow<ApiState>(ApiState.Idle)
    val registerState: StateFlow<ApiState> = _registerState

    fun setRegisterState(registerState: ApiState) {
        _registerState.value = registerState
    }

    fun register(
        fullName: String,
        identifier: String,
        phoneNumber: String,
        password: String,
        birthdate: String,
        gender: Gender
    ) {
        viewModelScope.launch {
            _registerState.value = ApiState.Loading

            try {
                val response = apiConfig.getAuthApiService().register(
                    RegisterRequest(
                        name = fullName,
                        identifier = identifier,
                        phoneNumber = "+62$phoneNumber",
                        password = password,
                        role = _userRole.value.toString(),
                        birthdate = if (_userRole.value == UserRole.CAREGIVER) "" else convertDateFormat(birthdate),
                        gender = gender.toString(),
                        address = "Jl. Lingkar, Pondok Cina, Kecamatan Beji, Kota Depok, Jawa Barat 16424",
                        latitude = 84.12312F,
                        longitude = 93.12312F
                    )
                )
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
                    dementiaStage = response.user?.dementiaStage,
                    patientId = response.user?.patientId ?: ""
                )
                userContext.saveSession(user)
                checkAuthentication()
                isLoginContext.setLoginState(true)
                _registerState.value = ApiState.Success
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _registerState.value = ApiState.Error(
                    message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                    description = errorResponse.errorDescription
                )
            } catch (e: Exception) {
                _registerState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    suspend fun logout() {
        userContext.logout()
    }


    // Assign Patient
    private val _searchPatientState = MutableStateFlow<ApiState>(ApiState.Idle)
    val searchPatientState: StateFlow<ApiState> = _searchPatientState

    fun setSearchPatientState(searchPatientState: ApiState) {
        _searchPatientState.value = searchPatientState
    }

    private val _selectedPatient = MutableLiveData<SelectedPatient?>()
    val selectedPatient: LiveData<SelectedPatient?> = _selectedPatient

    fun searchPatientByCredential(identifier: String, password: String) {
        viewModelScope.launch {
            _searchPatientState.value = ApiState.Loading

            try {
                val response = apiConfig.getCaregiverApiService().searchPatientByCredential(
                    SearchPatientByCredentialRequest(identifier, password)
                )
                _searchPatientState.value = ApiState.Success
                _selectedPatient.value = SelectedPatient(
                    id = response.patientId ?: "",
                    name = response.name ?: "",
                    age = response.age ?: 0
                )
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _searchPatientState.value = ApiState.Error(
                    message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                    description = errorResponse.errorDescription
                )
                _selectedPatient.value = null
            } catch (e: Exception) {
                _searchPatientState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
                _selectedPatient.value = null
            }
        }
    }

    private val _assignPatientState = MutableStateFlow<ApiState>(ApiState.Idle)
    val assignPatientState: StateFlow<ApiState> = _assignPatientState

    fun setAssignPatientState(assignPatientState: ApiState) {
        _assignPatientState.value = assignPatientState
    }

    fun updateAssignedPatient(patientId: String) {
        viewModelScope.launch {
            _assignPatientState.value = ApiState.Loading

            try {
                apiConfig.getCaregiverApiService().updateAssignedPatient(
                    UpdateAssignedPatientRequest(patientId)
                )
                _assignPatientState.value = ApiState.Success
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _assignPatientState.value = ApiState.Error(
                    message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                    description = errorResponse.errorDescription
                )
            } catch (e: Exception) {
                _assignPatientState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }


    // SPLASH SCREEN
    private val _authenticatedState = MutableStateFlow<AuthenticatedState>(AuthenticatedState.Unassigned)
    val authenticatedState: StateFlow<AuthenticatedState> get() = _authenticatedState

    fun checkAuthentication() {
        viewModelScope.launch {
            userContext.getSession()
                .collect { user ->
                    if (user.token.isEmpty()) {
                        _authenticatedState.value = AuthenticatedState.Unauthenticated
                    } else if (user.role == UserRole.CAREGIVER && user.patientId.isNullOrEmpty()) {
                        _authenticatedState.value = AuthenticatedState.Unassigned
                    } else {
                        _authenticatedState.value = AuthenticatedState.Authenticated
                    }
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

    private fun convertDateFormat(input: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.getDefault())

        val date = inputFormat.parse(input)

        return outputFormat.format(date)
    }
}