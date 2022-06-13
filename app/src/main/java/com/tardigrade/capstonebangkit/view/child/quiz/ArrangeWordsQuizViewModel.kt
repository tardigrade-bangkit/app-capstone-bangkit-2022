package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.model.ArrangeWordsQuestion
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ArrangeWordsQuizViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String
) : ViewModel() {
    private val _arrangeWordsQuestion = MutableLiveData<Result<ArrangeWordsQuestion>>()
    val arrangeWordsQuestion: MutableLiveData<Result<ArrangeWordsQuestion>> = _arrangeWordsQuestion

    private val _selectedWords = MutableLiveData<ArrayList<String>>()
    val selectedWords: MutableLiveData<ArrayList<String>> = _selectedWords

    private val _unselectedWords = MutableLiveData<ArrayList<String>>()
    val unselectedWords: MutableLiveData<ArrayList<String>> = _unselectedWords

    fun getSentence() : String {
        var sentence = ""
        selectedWords.value?.let {
            for (word in it) {
                sentence += word
                if (it.last().toString() != word) sentence += ' '
            }
        }
        return sentence
    }

    fun getArrangeWordsQuestion(questionId: Int) {
//        _listMaterialContent.value = Result.Loading

        viewModelScope.launch {
            try {
                val arrangeWords = lessonRepository.getArrangeWordsQuestion(token, questionId)

                _arrangeWordsQuestion.value = Result.Success(arrangeWords)
            } catch (httpEx: HttpException) {
                if (httpEx.code() == 500) {
                    _arrangeWordsQuestion.value = Result.Error("Server error")
                }

                httpEx.response()?.errorBody()?.let {
                    val errorResponse = getErrorResponse(it)

                    _arrangeWordsQuestion.value = Result.Error(errorResponse.msg)
                }
            } catch (genericEx: Exception) {
                _arrangeWordsQuestion.value = Result.Error(genericEx.message ?: "")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val lessonRepository: LessonRepository, private val token: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ArrangeWordsQuizViewModel(lessonRepository, token) as T
        }
    }
}