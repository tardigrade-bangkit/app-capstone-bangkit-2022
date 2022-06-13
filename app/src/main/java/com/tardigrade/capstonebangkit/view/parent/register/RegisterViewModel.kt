package com.tardigrade.capstonebangkit.view.parent.register

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.api.NewUser
import com.tardigrade.capstonebangkit.data.repository.AuthRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _registered = MutableLiveData<Result<String>>()
    val registered: LiveData<Result<String>> = _registered

    fun register(newUser: NewUser) {
        _registered.value = Result.Loading

        viewModelScope.launch {
            try {
                authRepository.register(newUser)

                _registered.value = Result.Success(newUser.email)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _registered.value = Result.Error("Server error")
                }

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