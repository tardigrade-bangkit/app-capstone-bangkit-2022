package com.tardigrade.capstonebangkit.view.child

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.data.model.LessonContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LessonContentViewModel : ViewModel() {
    private val _listLessonContent = MutableLiveData<Result<List<LessonContent>>>()
    val listLessonContent: MutableLiveData<Result<List<LessonContent>>> = _listLessonContent

    var currentLessonContent: LessonContent? = null
    var currentLesson: Lesson? = null
    var lessonContents: List<LessonContent>? = null

    private var currentLessonContentIdx = -1

    fun getNextLessonContent() {
        currentLessonContent =
            if (currentLessonContentIdx == -1) {
                    currentLessonContentIdx = 0
                    lessonContents?.get(0)
                } else {
                    currentLessonContentIdx += 1
                    lessonContents?.get(currentLessonContentIdx)
                }
    }

    fun getPreviousContent() {
        currentLessonContent =
            if (currentLessonContentIdx == -1) {
                currentLessonContentIdx = 0
                lessonContents?.get(0)
            } else {
                currentLessonContentIdx -= 1
                lessonContents?.get(currentLessonContentIdx)
            }
    }

    fun getLessonContent(lessonId: Int, lessonRepository: LessonRepository, token: String) {
//        _listLessonContent = Result.Loading

        viewModelScope.launch {
            try {
                val lessons = lessonRepository.getLesson(token, lessonId)

                _listLessonContent.value = Result.Success(lessons)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _listLessonContent.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _listLessonContent.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _listLessonContent.value = Result.Error(genericEx.message ?: "")
            }
        }
    }
}