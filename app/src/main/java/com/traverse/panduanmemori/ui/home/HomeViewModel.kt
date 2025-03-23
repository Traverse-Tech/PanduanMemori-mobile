package com.traverse.panduanmemori.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.traverse.panduanmemori.data.contexts.IsLoginContext
import com.traverse.panduanmemori.data.contexts.isLoginDataStore
import kotlinx.coroutines.flow.first

class HomeViewModel(context: Context) : ViewModel() {
    private val isLoginContext = IsLoginContext.getInstance(context.isLoginDataStore)

    suspend fun isShowWelcomeToast(): Boolean {
        return isLoginContext.getLoginState().first()
    }

    suspend fun resetLoginContext() {
        isLoginContext.setLoginState(false)
    }
}
