package com.traverse.panduanmemori.ui.task

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.traverse.panduanmemori.data.dataclasses.ActivitiesItem
import com.traverse.panduanmemori.data.dataclasses.DataItem
import com.traverse.panduanmemori.data.dataclasses.ErrorResponse
import com.traverse.panduanmemori.data.dataclasses.User
import com.traverse.panduanmemori.data.repositories.ApiConfig
import com.traverse.panduanmemori.data.repositories.ApiState
import com.traverse.panduanmemori.data.repositories.DEFAULT_ERROR_MESSAGE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel(context: Context) : ViewModel() {
    private val apiConfig = ApiConfig(context)

    private val _initState = MutableStateFlow<ApiState>(ApiState.Idle)
    val initState: StateFlow<ApiState> = _initState

    private val _caregiverPatients = MutableStateFlow<List<DataItem>>(emptyList())
    val caregiverPatients: StateFlow<List<DataItem>> get() = _caregiverPatients

    private val _patientActivities = MutableStateFlow<List<ActivitiesItem>>(emptyList())
    val patientActivities: StateFlow<List<ActivitiesItem>> get() = _patientActivities

    private fun convertDateFormat(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    suspend fun getInitPageData(date: String) {
        _initState.value = ApiState.Loading

        try {
            _caregiverPatients.value = apiConfig.getCaregiverApiService().getCaregiverPatients().data as List<DataItem>

            val formattedDate = convertDateFormat(date)
            val getPatientActivitiesResponse =
                _caregiverPatients.value[0].id?.let {
                    apiConfig.getActivityApiService().getActivities(
                        it, formattedDate, formattedDate)
                }
            if (getPatientActivitiesResponse?.activities != null) {
                _patientActivities.value = getPatientActivitiesResponse.activities as List<ActivitiesItem>
            }
            _initState.value = ApiState.Success
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            _initState.value = ApiState.Error(
                message = errorResponse.responseMessage ?: DEFAULT_ERROR_MESSAGE,
                description = errorResponse.errorDescription
            )
        } catch (e: Exception) {
            _initState.value = ApiState.Error(DEFAULT_ERROR_MESSAGE, e.message)
        }
    }
}