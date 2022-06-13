package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.ShortAnswerQuestion
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShortAnswerQuizViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
) : ViewModel() {
    private val _shortAnswerQuestion = MutableLiveData<Result<ShortAnswerQuestion>>()
    val shortAnswerQuestion: MutableLiveData<Result<ShortAnswerQuestion>> = _shortAnswerQuestion

    fun getShortAnswerQuestion(questionId: Int) {
//        _listMaterialContent.value = Result.Loading

        viewModelScope.launch {
            try {
                val shortAnswer = lessonRepository.getShortAnswerQuestion(token, questionId)

                _shortAnswerQuestion.value = Result.Success(shortAnswer)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _shortAnswerQuestion.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _shortAnswerQuestion.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _shortAnswerQuestion.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShortAnswerQuizViewModel(lessonRepository, token) as T
        }
    }
}