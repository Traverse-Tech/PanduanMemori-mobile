package com.traverse.panduanmemori.data.contexts

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.isLoginDataStore: DataStore<Preferences> by preferencesDataStore(name = "is_login")

class IsLoginContext private constructor(private val dataStore: DataStore<Preferences>){
    suspend fun setLoginState(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn.toString()
        }
    }

    fun getLoginState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            (preferences[IS_LOGGED_IN] ?: "false").toBoolean()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: IsLoginContext? = null

        private val IS_LOGGED_IN = stringPreferencesKey("is_logged_in")

        fun getInstance(dataStore: DataStore<Preferences>): IsLoginContext {
            return INSTANCE ?: synchronized(this) {
                val instance = IsLoginContext(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}