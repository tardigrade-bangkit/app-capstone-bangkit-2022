package com.tardigrade.capstonebangkit.view.parent.placementquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tardigrade.capstonebangkit.data.api.NewUser
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getErrorResponse
import com.tardigrade.capstonebangkit.view.parent.profile.ProfileViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PlacementQuizViewModel(
    private val lessonRepository: LessonRepository,
    private val token: String,
    private val childId: Int
) : ViewModel() {
    private val _listQuizContent = MutableLiveData<List<QuizContent>>()
    val listQuizContent: MutableLiveData<List<QuizContent>> = _listQuizContent

    fun getData() {
        viewModelScope.launch {
            try {
                val lesson = lessonRepository.getLessonsByLevel(token, 0)[0]
                Log.d("TAG", "lesson: $lesson")
                val content = lessonRepository.getLesson(token, lesson.id)[0]
                Log.d("TAG", "content: $content")

                val quiz = lessonRepository.getQuiz(token, content.quizzesId ?: 0).sortedBy {
                    it.order
                }
                Log.d("TAG", "quiz: $quiz")

                val questions = mutableListOf<MultipleChoiceQuestion>()
                quiz.forEach {
                    questions.add(lessonRepository.getMultipleChoiceQuestion(token, it.id))
                }
                Log.d("TAG", "questions: $questions")


            } catch (genericEx: Exception) {
                Log.d("TAG", "register: $genericEx")
                Log.d("TAG", "register: ${genericEx.stackTrace.toString()}")

            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val lessonRepository: LessonRepository,
        private val token: String,
        private val childId: Int
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlacementQuizViewModel(lessonRepository, token, childId) as T
        }
    }
}