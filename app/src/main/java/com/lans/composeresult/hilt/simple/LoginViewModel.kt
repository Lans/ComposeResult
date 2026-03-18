package com.lans.composeresult.hilt.simple

import androidx.lifecycle.ViewModel
import com.lans.composeresult.core.ResultStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val resultStore: ResultStore
) : ViewModel() {

    fun performLogin(inputName: String, onSuccess: () -> Boolean) {
        if (inputName.isNotBlank()) {
            // 核心逻辑：业务完成后发送结果，全应用单例会立即感知
            resultStore.setResult(inputName, tag = "user_name")
            onSuccess()
        }
    }
}