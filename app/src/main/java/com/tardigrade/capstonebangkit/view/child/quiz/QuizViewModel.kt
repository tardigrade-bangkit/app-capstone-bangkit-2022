package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class QuizViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
) : ViewModel() {
    private val _listQuizContent = MutableLiveData<Result<List<QuizContent>>>()
    val listQuizContent: MutableLiveData<Result<List<QuizContent>>> = _listQuizContent

    private val _currentQuizContent = MutableLiveData<QuizContent?>()
    val currentQuizContent: MutableLiveData<QuizContent?> = _currentQuizContent

    fun setCurrentQuizContent(quizContent: QuizContent?) {
        _currentQuizContent.value = quizContent
    }

    fun getQuiz(quizId: Int) {
//        _listMaterialContent.value = Result.Loading

        viewModelScope.launch {
            try {
                val quiz = lessonRepository.getQuiz(token, quizId)

                _listQuizContent.value = Result.Success(quiz)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _listQuizContent.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _listQuizContent.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _listQuizContent.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuizViewModel(lessonRepository, token) as T
        }
    }
}