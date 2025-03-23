package com.traverse.panduanmemori.ui.activities

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.traverse.panduanmemori.data.models.Activity
import com.traverse.panduanmemori.data.repositories.ApiConfig
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.data.repositories.DEFAULT_ERROR_MESSAGE
import com.traverse.panduanmemori.data.dataclasses.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ActivitiesViewModel(context: Context) : ViewModel() {
    private val apiConfig = ApiConfig(context)

    private val _activitiesState = MutableStateFlow<ApiState>(ApiState.Idle)
    val activitiesState: StateFlow<ApiState> = _activitiesState

    private val _activities = MutableStateFlow<List<ActivityOccurrence>>(emptyList())
    val activities: StateFlow<List<ActivityOccurrence>> = _activities


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadActivities(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _activitiesState.value = ApiState.Loading
            try {
                val response = apiConfig.getActivityApiService().getActivitiesInRange(
                    GetActivitiesInRangeRequest(
                        startDate = startDate.format(DateTimeFormatter.ISO_DATE),
                        endDate = endDate.format(DateTimeFormatter.ISO_DATE)
                    )
                )

                _activities.value = response.activities.flatMap { it.occurrences }

                _activitiesState.value = ApiState.Success
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                _activitiesState.value = ApiState.Error(
                    message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                    description = errorResponse.errorDescription
                )
            } catch (e: Exception) {
                _activitiesState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    fun createActivity(activity: CreateActivityRequest) {
        viewModelScope.launch {
            _activitiesState.value = ApiState.Loading
            try {
                apiConfig.getActivityApiService().createActivity(activity)
                loadActivities(LocalDate.now(), LocalDate.now())
                _activitiesState.value = ApiState.Success
            } catch (e: Exception) {
                _activitiesState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    fun updateActivity(id: String, activity: UpdateActivityRequest) {
        viewModelScope.launch {
            _activitiesState.value = ApiState.Loading
            try {
                apiConfig.getActivityApiService().updateActivity(id, activity)
                loadActivities(LocalDate.now(), LocalDate.now())
                _activitiesState.value = ApiState.Success
            } catch (e: Exception) {
                _activitiesState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    fun deleteFutureActivity(id: String) {
        viewModelScope.launch {
            _activitiesState.value = ApiState.Loading
            try {
                apiConfig.getActivityApiService().deleteFutureActivity(id)
                loadActivities(LocalDate.now(), LocalDate.now())
                _activitiesState.value = ApiState.Success
            } catch (e: Exception) {
                _activitiesState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    fun deleteAllActivity(id: String) {
        viewModelScope.launch {
            _activitiesState.value = ApiState.Loading
            try {
                apiConfig.getActivityApiService().deleteAllActivity(id)
                loadActivities(LocalDate.now(), LocalDate.now())
                _activitiesState.value = ApiState.Success
            } catch (e: Exception) {
                _activitiesState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }

    fun completeActivity(activityId: String) {
        viewModelScope.launch {
            _activitiesState.value = ApiState.Loading
            try {
                apiConfig.getActivityApiService().completeActivityOccurence(activityId)
                _activitiesState.value = ApiState.Success
            } catch (e: Exception) {
                _activitiesState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
            }
        }
    }
}
