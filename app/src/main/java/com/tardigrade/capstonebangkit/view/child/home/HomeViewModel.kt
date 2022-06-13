package com.tardigrade.capstonebangkit.view.child.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
) : ViewModel() {
    private val _listLesson = MutableLiveData<Result<List<Lesson>>>()
    val listLesson: MutableLiveData<Result<List<Lesson>>> = _listLesson

    fun getLessons(level: Int) {
        _listLesson.value = Result.Loading

        viewModelScope.launch {
            try {
                val lessons = lessonRepository.getLessonsByLevel(token, level)

                _listLesson.value = Result.Success(lessons)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _listLesson.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _listLesson.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _listLesson.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(lessonRepository, token) as T
        }
    }
}