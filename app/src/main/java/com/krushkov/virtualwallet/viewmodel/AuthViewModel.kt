package com.krushkov.virtualwallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krushkov.virtualwallet.data.api.ApiProvider
import com.krushkov.virtualwallet.data.api.RetrofitClient
import com.krushkov.virtualwallet.data.model.auth.LoginRequest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Loading)
    val authState: LiveData<AuthState> = _authState

    fun checkSession() {
        viewModelScope.launch {

            _authState.value = AuthState.Loading

            try {
                val response = ApiProvider.authApi.getMe()

                if (response.isSuccessful) {
                    val user = response.body()?.data

                    if (user != null) {
                        _authState.value = AuthState.Authenticated(user)
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }

                } else {
                    _authState.value = AuthState.Unauthenticated
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun login(identifier: String, password: String) {
        viewModelScope.launch {

            _authState.value = AuthState.Loading

            try {
                val loginResponse = ApiProvider.authApi.login(
                    LoginRequest(identifier, password)
                )

                if (loginResponse.isSuccessful) {

                    val user = loginResponse.body()?.data

                    if (user != null) {

                        val meResponse = ApiProvider.authApi.getMe()

                        if (meResponse.isSuccessful) {
                            _authState.value = AuthState.Authenticated(user)
                        } else {
                            _authState.value =
                                AuthState.Error("Session not established")
                        }

                    } else {
                        _authState.value =
                            AuthState.Error("Empty user data")
                    }

                } else {
                    _authState.value = AuthState.Error("Login failed")
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Network error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {

            try {
                val response = ApiProvider.authApi.logout()

                if (response.isSuccessful) {
                    _authState.value = AuthState.Unauthenticated
                } else {
                    _authState.value = AuthState.Error("Logout failed")
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Network error")
            }
        }
    }
}