package com.example.flerkthsound.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.flerkthsound.network.RetrofitInstance
import com.example.flerkthsound.network.models.*
import com.example.flerkthsound.utils.TokenManager
import com.example.flerkthsound.network.models.User

// Объявите sealed class ПЕРВЫМ в файле
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: com.example.flerkthsound.network.models.User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
    val loginState: StateFlow<AuthState> = _loginState

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
    val registerState: StateFlow<AuthState> = _registerState

    // ... остальной код AuthViewModel без изменений ...

    fun login(context: Context, email: String, password: String) {
        _loginState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                println("🔄 Attempting login with: $email")
                println("📡 Base URL: http://10.0.2.2:3000/api/")

                val apiService = RetrofitInstance.createApiService(context)
                val response = apiService.login(LoginRequest(email, password))

                println("📡 Response code: ${response.code()}")
                println("📡 Response message: ${response.message()}")
                println("📡 Response headers: ${response.headers()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    println("📡 Response body: $body")

                    if (body?.success == true) {
                        println("✅ Login successful")
                        body.user?.let { user ->
                            TokenManager.saveToken(
                                context = context,
                                token = body.token!!,
                                userId = user.id,
                                userRole = user.role
                            )
                        }
                        _loginState.value = AuthState.Success(body.user!!)
                    } else {
                        val errorMsg = body?.error ?: "Unknown server error"
                        println("❌ Server error: $errorMsg")
                        _loginState.value = AuthState.Error(errorMsg)
                    }
                } else {
                    // Детальная информация об ошибке HTTP
                    val errorBody = response.errorBody()?.string()
                    println("❌ HTTP error: ${response.code()} - ${response.message()}")
                    println("❌ Error body: $errorBody")

                    val errorMsg = when (response.code()) {
                        401 -> "Invalid credentials"
                        404 -> "Server endpoint not found"
                        500 -> "Internal server error"
                        else -> "HTTP ${response.code()}: ${response.message()}"
                    }
                    _loginState.value = AuthState.Error(errorMsg)
                }

            } catch (e: Exception) {
                println("❌ Network error: ${e.message}")
                e.printStackTrace()
                _loginState.value = AuthState.Error("Network error: ${e.message}")
            }
        }
    }

    fun register(context: Context, name: String, email: String, password: String) {
        _registerState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                println("🔄 Attempting registration: $email")
                val apiService = RetrofitInstance.createApiService(context)
                val response = apiService.register(RegisterRequest(email, password, name))

                println("📡 Registration response code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    println("📡 Registration body: $body")

                    if (body?.success == true) {
                        println("✅ Registration successful")
                        body.user?.let { user ->
                            TokenManager.saveToken(
                                context = context,
                                token = body.token!!,
                                userId = user.id,
                                userRole = user.role
                            )
                        }
                        _registerState.value = AuthState.Success(body.user!!)
                    } else {
                        val errorMsg = body?.error ?: "Unknown registration error"
                        println("❌ Registration error: $errorMsg")

                        // Специальная обработка для "User already exists"
                        if (errorMsg.contains("already exists", ignoreCase = true)) {
                            _registerState.value = AuthState.Error("User with this email already exists. Please use a different email or login.")
                        } else {
                            _registerState.value = AuthState.Error(errorMsg)
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("❌ Registration HTTP error: ${response.code()} - $errorBody")
                    _registerState.value = AuthState.Error("Registration failed: ${response.code()}")
                }

            } catch (e: Exception) {
                println("❌ Registration network error: ${e.message}")
                _registerState.value = AuthState.Error("Network error: ${e.message}")
            }
        }
    }

    private fun handleAuthResponse(
        context: Context,
        response: retrofit2.Response<ApiResponse<LoginResponse>>,
        state: MutableStateFlow<AuthState>
    ) {
        if (response.isSuccessful && response.body()?.success == true) {
            val body = response.body()!!
            TokenManager.saveToken(
                context = context,
                token = body.token!!,
                userId = body.user?.id,
                userRole = body.user?.role
            )
            state.value = AuthState.Success(body.user!!)
        } else {
            val errorMessage = response.body()?.error ?: "Unknown error occurred"
            state.value = AuthState.Error(errorMessage)
        }
    }

    fun logout(context: Context) {
        TokenManager.clearToken(context)
        _loginState.value = AuthState.Idle
        _registerState.value = AuthState.Idle
    }

    fun resetLoginState() { _loginState.value = AuthState.Idle }
    fun resetRegisterState() { _registerState.value = AuthState.Idle }


}



//package com.example.flerkthsound.viewmodels
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import com.example.flerkthsound.network.RetrofitInstance
//import com.example.flerkthsound.network.models.*
//import com.example.flerkthsound.utils.TokenManager
//
//class AuthViewModel : ViewModel() {
//
//    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)
//    val loginState: StateFlow<AuthState> = _loginState
//
//    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)
//    val registerState: StateFlow<AuthState> = _registerState
//
//    fun login(context: Context, email: String, password: String) {
//        _loginState.value = AuthState.Loading
//        viewModelScope.launch {
//            try {
//                println("🔄 Attempting login with: $email")
//
//                val apiService = RetrofitInstance.createApiService(context)
//                val response = apiService.login(LoginRequest(email, password))
//
//                println("📡 Response code: ${response.code()}")
//                println("📡 Response body: ${response.body()}")
//
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    if (body?.success == true) {
//                        println("✅ Login successful")
//                        body.user?.let { user ->
//                            TokenManager.saveToken(
//                                context = context,
//                                token = body.token!!,
//                                userId = user.id,
//                                userRole = user.role
//                            )
//                        }
//                        _loginState.value = AuthState.Success(body.user!!)
//                    } else {
//                        val errorMsg = body?.error ?: "Unknown server error"
//                        println("❌ Server error: $errorMsg")
//                        _loginState.value = AuthState.Error(errorMsg)
//                    }
//                } else {
//                    val errorMsg = "HTTP ${response.code()}: ${response.message()}"
//                    println("❌ HTTP error: $errorMsg")
//                    _loginState.value = AuthState.Error(errorMsg)
//                }
//
//            } catch (e: Exception) {
//                println("❌ Network error: ${e.message}")
//                e.printStackTrace()
//                _loginState.value = AuthState.Error("Network error: ${e.message}")
//            }
//        }
//    }
//
//    fun register(context: Context, name: String, email: String, password: String) {
//        _registerState.value = AuthState.Loading
//        viewModelScope.launch {
//            try {
//                val apiService = RetrofitInstance.createApiService(context)
//                val response = apiService.register(RegisterRequest(email, password, name))
//                handleAuthResponse(context, response, _registerState)
//            } catch (e: Exception) {
//                _registerState.value = AuthState.Error("Network error: ${e.message}")
//            }
//        }
//    }
//
//    private fun handleAuthResponse(
//        context: Context,
//        response: retrofit2.Response<ApiResponse<LoginResponse>>,
//        state: MutableStateFlow<AuthState>
//    ) {
//        if (response.isSuccessful && response.body()?.success == true) {
//            val body = response.body()!!
//            TokenManager.saveToken(
//                context = context,
//                token = body.token!!,
//                userId = body.user?.id,
//                userRole = body.user?.role
//            )
//            state.value = AuthState.Success(body.user!!)
//        } else {
//            val errorMessage = response.body()?.error ?: "Unknown error occurred"
//            state.value = AuthState.Error(errorMessage)
//        }
//    }
//
//    fun logout(context: Context) {
//        TokenManager.clearToken(context)
//        _loginState.value = AuthState.Idle
//        _registerState.value = AuthState.Idle
//    }
//
//    fun resetLoginState() { _loginState.value = AuthState.Idle }
//    fun resetRegisterState() { _registerState.value = AuthState.Idle }
//}
//
//// AuthState должен быть в ЭТОМ ЖЕ ФАЙЛЕ
//open class AuthState {
//    object Idle : AuthState()
//    object Loading : AuthState()
//    data class Success(val user: User) : AuthState()
//    data class Error(val message: String) : AuthState()
//}