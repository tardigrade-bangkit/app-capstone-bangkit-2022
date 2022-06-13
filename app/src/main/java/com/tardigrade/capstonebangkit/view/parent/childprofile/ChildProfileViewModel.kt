package com.tardigrade.capstonebangkit.view.parent.childprofile

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.api.AddChild
import com.tardigrade.capstonebangkit.data.model.Avatar
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChildProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val token: String
) : ViewModel() {
    private var _avatars = MutableLiveData<Result<List<Avatar>>>()
    val avatars: LiveData<Result<List<Avatar>>> = _avatars

    private var _addChildrenResult = MutableLiveData<Result<ChildProfile>>()
    val addChildrenResult: LiveData<Result<ChildProfile>> = _addChildrenResult

    init {
        getAvatars()
    }

    fun getAvatars() {
        _avatars.value = Result.Loading

        viewModelScope.launch {
            try {
                val avatars = profileRepository.getAvatars()

                _avatars.value = Result.Success(avatars)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _avatars.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _avatars.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _avatars.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    fun addChildren(child: AddChild) {
        _addChildrenResult.value = Result.Loading

        viewModelScope.launch {
            try {
                val new = profileRepository.addChildren(token, child)

                _addChildrenResult.value = Result.Success(new)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _addChildrenResult.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _addChildrenResult.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _addChildrenResult.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val profileRepository: ProfileRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChildProfileViewModel(profileRepository, token) as T
        }
    }
}