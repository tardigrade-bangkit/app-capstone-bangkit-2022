package com.tardigrade.capstonebangkit.view.parent.dashboard

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.model.Achievement
import com.tardigrade.capstonebangkit.data.model.Badge
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val token: String
) : ViewModel() {
    private var _children = MutableLiveData<Result<List<ChildProfile>>>()
    val children: LiveData<Result<List<ChildProfile>>> = _children

    private var _progress = MutableLiveData<Result<List<Lesson>>>()
    val progress: LiveData<Result<List<Lesson>>> = _progress

    private var _badges = MutableLiveData<Result<List<Badge>>>()
    val badges: LiveData<Result<List<Badge>>> = _badges

    private var _achievements = MutableLiveData<Result<List<Achievement>>>()
    val achievements: LiveData<Result<List<Achievement>>> = _achievements

    init {
        getChildren()
    }

    fun getChildren() {
        _children.value = Result.Loading

        viewModelScope.launch {
            try {
                val children = profileRepository.getChildren(token)

                _children.value = Result.Success(children)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _children.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _children.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    fun getChildData(childProfile: ChildProfile) {
        getProgress(childProfile)
        getBadges(childProfile)
        getAchievements(childProfile)
    }

    private fun getAchievements(childProfile: ChildProfile) {
        _achievements.value = Result.Loading

        viewModelScope.launch {
            try {
                val achievements = profileRepository.getChildrenAchievements(token, childProfile)

                _achievements.value = Result.Success(achievements)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _achievements.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _achievements.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    private fun getProgress(childProfile: ChildProfile) {
        _progress.value = Result.Loading

        viewModelScope.launch {
            try {
                val lessons = profileRepository.getChildrenProgress(token, childProfile)

                _progress.value = Result.Success(lessons)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _progress.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _progress.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    private fun getBadges(childProfile: ChildProfile) {
        _badges.value = Result.Loading

        viewModelScope.launch {
            try {
                val badges = profileRepository.getChildrenBadges(token, childProfile)

                _badges.value = Result.Success(badges)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _badges.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _badges.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val profileRepository: ProfileRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(profileRepository, token) as T
        }
    }
}