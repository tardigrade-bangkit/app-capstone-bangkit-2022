package com.tardigrade.capstonebangkit.view.parent.login

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.repository.AuthRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loggedIn = MutableLiveData<Result<Boolean>>()
    val loggedIn: LiveData<Result<Boolean>> = _loggedIn

    fun login(email: String, password: String) {
        _loggedIn.value = Result.Loading

        viewModelScope.launch {
            try {
                val hasPin = authRepository.login(email, password)

                _loggedIn.value = Result.Success(hasPin)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _loggedIn.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _loggedIn.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val authRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(authRepository) as T
        }
    }
}