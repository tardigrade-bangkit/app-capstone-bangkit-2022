package com.tardigrade.capstonebangkit.view.child

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.LessonContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import com.tardigrade.capstonebangkit.view.child.home.HomeViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LessonContentViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
) : ViewModel() {
    private val _listLessonContent = MutableLiveData<Result<List<LessonContent>>>()
    val listLessonContent: MutableLiveData<Result<List<LessonContent>>> = _listLessonContent

    val currentLessonContent = MutableLiveData<LessonContent>()

    private var currentLessonContentIdx = -1

    fun getNextLessonContent(lessonContents: List<LessonContent> ) {
        currentLessonContent.value =
            if (currentLessonContentIdx == -1) {
                    currentLessonContentIdx = 0
                    lessonContents[0]
                } else {
                    currentLessonContentIdx += 1
                    lessonContents[currentLessonContentIdx]
                }
    }

    fun getLessonContent(lessonId: Int) {
//        _listLessonContent = Result.Loading

        viewModelScope.launch {
            try {
                val lessons = lessonRepository.getLesson(token, lessonId)

                _listLessonContent.value = Result.Success(lessons)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _listLessonContent.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _listLessonContent.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LessonContentViewModel(lessonRepository, token) as T
        }
    }
}