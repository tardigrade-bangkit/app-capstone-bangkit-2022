package com.tardigrade.capstonebangkit.view.parent.pin

import android.util.Log
import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.repository.AuthRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PinViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _pinResult = MutableLiveData<Result<Int>>()
    val pinResult: LiveData<Result<Int>> = _pinResult

    fun addPin(token: String, pin: String) {
        _pinResult.value = Result.Loading

        viewModelScope.launch {
            try {
                authRepository.addPin(token, pin)

                _pinResult.value = Result.Success(ADD_PIN)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _pinResult.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _pinResult.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    fun checkPin(token: String, pin: String) {
        _pinResult.value = Result.Loading

        viewModelScope.launch {
            try {
                authRepository.checkPin(token, pin)

                _pinResult.value = Result.Success(CHECK_PIN)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)
                    Log.d("TAG", "checkPin: $errorResponse")

                    _pinResult.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _pinResult.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val authRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PinViewModel(authRepository) as T
        }
    }

    companion object {
        const val ADD_PIN = 1
        const val CHECK_PIN = 2
    }
}