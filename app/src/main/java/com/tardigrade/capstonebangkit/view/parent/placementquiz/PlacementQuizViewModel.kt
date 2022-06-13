package com.tardigrade.capstonebangkit.view.parent.placementquiz

import androidx.lifecycle.*
import com.tardigrade.capstonebangkit.data.api.PostAnswerResponse
import com.tardigrade.capstonebangkit.data.model.Answer
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PlacementQuizViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String,
    private val profileRepository: ProfileRepository,

    ) : ViewModel() {
    private val _listQuizContent = MutableLiveData<Result<List<QuizContent>>>()
    val listQuizContent: LiveData<Result<List<QuizContent>>> = _listQuizContent

    private val _currentQuestion = MutableLiveData<Result<MultipleChoiceQuestion>>()
    val currentQuestion: LiveData<Result<MultipleChoiceQuestion>> = _currentQuestion

    private val _sendAnswerResult = MutableLiveData<Result<PostAnswerResponse>>()
    val sendAnswerResult: LiveData<Result<PostAnswerResponse>> = _sendAnswerResult

    private val _updateChildResult = MutableLiveData<Result<Unit>>()
    val updateChildResult: LiveData<Result<Unit>> = _updateChildResult

    init {
        getListQuestion()
    }

    fun getListQuestion() {
        _listQuizContent.value = Result.Loading

        viewModelScope.launch {
            try {
                val lesson = lessonRepository.getLessonsByLevel(token, 0)[0]
                val content = lessonRepository.getLesson(token, lesson.id)[0]
                val quizzes = lessonRepository.getQuiz(token, content.quizzesId ?: 0).sortedBy {
                    it.order
                }

                _listQuizContent.value = Result.Success(quizzes)
                getQuestion(quizzes[0])
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

    fun getQuestion(quizContent: QuizContent) {
        _currentQuestion.value = Result.Loading

        viewModelScope.launch {
            try {
                val question = lessonRepository.getMultipleChoiceQuestion(token, quizContent.id)

                _currentQuestion.value = Result.Success(question)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _currentQuestion.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _currentQuestion.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _currentQuestion.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    fun sendAnswer(listAnswer: List<Answer>) {
        _sendAnswerResult.value = Result.Loading

        viewModelScope.launch {
            try {
                val result = lessonRepository.sendAnswer(token, listAnswer)

                _sendAnswerResult.value = Result.Success(result)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _sendAnswerResult.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _sendAnswerResult.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _sendAnswerResult.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    fun updateChild(childProfile: ChildProfile) {
        _updateChildResult.value = Result.Loading

        viewModelScope.launch {
            try {
                profileRepository.updateChild(token, childProfile)

                _updateChildResult.value = Result.Success(Unit)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _updateChildResult.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _updateChildResult.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _updateChildResult.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val lessonRepository: LessonRepository,
        private val token: String,
        private val profileRepository: ProfileRepository
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlacementQuizViewModel(lessonRepository, token, profileRepository) as T
        }
    }
}