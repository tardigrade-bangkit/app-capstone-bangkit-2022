package com.tardigrade.capstonebangkit.view.parent.childprofile

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.model.Avatar
import com.tardigrade.capstonebangkit.data.repository.ChildrenDataRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChildProfileViewModel(private val childrenDataRepository: ChildrenDataRepository) : ViewModel() {
    private var _avatars = MutableLiveData<Result<List<Avatar>>>()
    val avatars: LiveData<Result<List<Avatar>>> = _avatars

    init {
        getAvatars()
    }

    fun getAvatars() {
        _avatars.value = Result.Loading

        viewModelScope.launch {
            try {
                val avatars = childrenDataRepository.getAvatars()

                _avatars.value = Result.Success(avatars)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _avatars.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _avatars.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val childrenDataRepository: ChildrenDataRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChildProfileViewModel(childrenDataRepository) as T
        }
    }
}