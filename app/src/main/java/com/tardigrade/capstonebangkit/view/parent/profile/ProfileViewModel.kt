package com.tardigrade.capstonebangkit.view.parent.profile

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.model.User
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val token: String
) : ViewModel() {
    private var _children = MutableLiveData<Result<List<ChildProfile>>>()
    val children: LiveData<Result<List<ChildProfile>>> = _children

    private var _myProfile = MutableLiveData<Result<User>>()
    val myProfile: LiveData<Result<User>> = _myProfile

    init {
        getMyProfile()
        getChildren()
    }

    fun getMyProfile() {
        _myProfile.value = Result.Loading

        viewModelScope.launch {
            try {
                _myProfile.value = Result.Success(profileRepository.getSelf(token))
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _myProfile.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _myProfile.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _myProfile.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    fun getChildren() {
        _children.value = Result.Loading

        viewModelScope.launch {
            try {
                val children = profileRepository.getChildren(token)

                _children.value = Result.Success(children)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _children.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _children.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _children.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val profileRepository: ProfileRepository,
        private val token: String
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(profileRepository, token) as T
        }
    }
}