package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MultipleChoiceQuizViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
) : ViewModel() {
    private val _multipleChoiceQuestion = MutableLiveData<Result<MultipleChoiceQuestion>>()
    val multipleChoiceQuestion: MutableLiveData<Result<MultipleChoiceQuestion>> = _multipleChoiceQuestion

    fun getMultipleChoiceQuestion(questionId: Int) {
//        _listMaterialContent.value = Result.Loading

        viewModelScope.launch {
            try {
                val multipleChoice = lessonRepository.getMultipleChoiceQuestion(token, questionId)

                _multipleChoiceQuestion.value = Result.Success(multipleChoice)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _multipleChoiceQuestion.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _multipleChoiceQuestion.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _multipleChoiceQuestion.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MultipleChoiceQuizViewModel(lessonRepository, token) as T
        }
    }
}