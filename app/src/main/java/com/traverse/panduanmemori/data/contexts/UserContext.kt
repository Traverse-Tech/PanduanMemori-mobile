package com.traverse.panduanmemori.data.contexts

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.traverse.panduanmemori.data.models.Gender
import com.traverse.panduanmemori.data.models.User
import com.traverse.panduanmemori.data.models.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserContext private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = user.token
            preferences[NAME] = user.name
            preferences[PHONE_NUMBER] = user.phoneNumber
            preferences[EMAIL] = user.email ?: ""
            preferences[REGISTRATION_NUMBER] = user.registrationNumber ?: ""
            preferences[ROLE] = user.role.toString()
            preferences[ADDRESS] = user.address
            preferences[BIRTHDATE] = user.birthdate
            preferences[GENDER] = user.gender.toString()
            preferences[DEMENTIA_STAGE] = user.dementiaStage.toString()
            preferences[IS_ASSIGNED_TO_PATIENT] = user.isAssignedToPatient ?: false
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                token = preferences[TOKEN] ?: "",
                name = preferences[NAME] ?: "",
                phoneNumber = preferences[PHONE_NUMBER] ?: "",
                email = preferences[EMAIL] ?: "" ,
                registrationNumber = preferences[REGISTRATION_NUMBER] ?: "",
                role = try {
                    UserRole.valueOf(preferences[ROLE] ?: "CAREGIVER")
                } catch (e: IllegalArgumentException) {
                    UserRole.CAREGIVER
                },
                address = preferences[ADDRESS] ?: "",
                birthdate = preferences[BIRTHDATE] ?: "",
                gender = try {
                    Gender.valueOf(preferences[GENDER] ?: "MAN")
                } catch (e: IllegalArgumentException) {
                    Gender.MAN
                },
                dementiaStage = preferences[DEMENTIA_STAGE] ?: "",
                isAssignedToPatient = preferences[IS_ASSIGNED_TO_PATIENT]
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserContext? = null

        private val TOKEN = stringPreferencesKey("token")
        private val NAME = stringPreferencesKey("name")
        private val PHONE_NUMBER = stringPreferencesKey("phoneNumber")
        private val EMAIL = stringPreferencesKey("email")
        private val REGISTRATION_NUMBER = stringPreferencesKey("registrationNumber")
        private val ROLE = stringPreferencesKey("role")
        private val ADDRESS = stringPreferencesKey("address")
        private val BIRTHDATE = stringPreferencesKey("birthdate")
        private val GENDER = stringPreferencesKey("gender")
        private val DEMENTIA_STAGE = stringPreferencesKey("dementiaStage")
        private val IS_ASSIGNED_TO_PATIENT = booleanPreferencesKey("isAssignedToPatient")

        fun getInstance(dataStore: DataStore<Preferences>): UserContext {
            return INSTANCE ?: synchronized(this) {
                val instance = UserContext(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}