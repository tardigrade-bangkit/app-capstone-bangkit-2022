package com.tardigrade.capstonebangkit.view.parent.register

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.repository.AuthRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _registered = MutableLiveData<Result<String>>()
    val registered: LiveData<Result<String>> = _registered

    fun register(name: String, email: String, password: String) {
        _registered.value = Result.Loading

        viewModelScope.launch {
            try {
                authRepository.register(name, email, password)

                _registered.value = Result.Success(email)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _registered.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _registered.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val authRepository: AuthRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterViewModel(authRepository) as T
        }
    }
}