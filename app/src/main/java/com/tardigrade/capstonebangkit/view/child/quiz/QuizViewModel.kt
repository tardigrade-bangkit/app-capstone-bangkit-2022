package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.api.PostAnswerResponse
import com.tardigrade.capstonebangkit.data.model.Answer
import com.tardigrade.capstonebangkit.data.model.MaterialContent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import com.tardigrade.capstonebangkit.view.child.material.MaterialViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class QuizViewModel : ViewModel() {
    private val _listQuizContent = MutableLiveData<Result<List<QuizContent>>>()
    val listQuizContent: MutableLiveData<Result<List<QuizContent>>> = _listQuizContent

    private val _currentQuizContent = MutableLiveData<QuizContent?>()
    val currentQuizContent: MutableLiveData<QuizContent?> = _currentQuizContent

    private val _sendAnswerResult = MutableLiveData<Result<PostAnswerResponse>>()
    val sendAnswerResult: LiveData<Result<PostAnswerResponse>> = _sendAnswerResult

    var quizContent: QuizContent? = null
    var listAnswer: List<Answer>? = null

    fun setAnswer(questionId: Int, answer: String) {
        var found = false
        val listItr = listAnswer?.listIterator()
        if (listItr != null) {
            while (listItr.hasNext()) {
                if (listItr.next().questionId == questionId) {
                    listItr.next().answer = answer
                    found = true
                }
            }
        }

        if (!found) {
            val newAnswer = Answer(questionId, answer)
            val newList = ArrayList<Answer>()
            listAnswer?.let { newList.addAll(it) }
            newList.add(newAnswer)
            listAnswer = newList
        }
    }

    fun setCurrentQuizContent(quizContent: QuizContent?) {
        _currentQuizContent.value = quizContent
        this.quizContent = quizContent
    }

    fun getQuiz(quizId: Int, lessonRepository: LessonRepository, token: String) {
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

    fun sendAnswer(listAnswer: List<Answer>, lessonRepository: LessonRepository, token: String) {
        _sendAnswerResult.value = Result.Loading

        viewModelScope.launch {
            try {
                val result = lessonRepository.sendAnswer(token, listAnswer)

                _sendAnswerResult.value = Result.Success(result)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _sendAnswerResult.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _sendAnswerResult.value = Result.Error(genericEx.message ?: "")
            }
        }
    }
}