package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService
import com.tardigrade.capstonebangkit.data.api.PostAnswerBody
import com.tardigrade.capstonebangkit.data.model.Answer

class LessonRepository(private val apiService: ApiService) {
    suspend fun getLessonsByLevel(token: String, level: Int) =
        apiService.getLessonsByLevel(token, level).lessons

    suspend fun getLesson(token: String, lessonId: Int) =
        apiService.getLesson(token, lessonId).lessonContents

    suspend fun getMaterial(token: String, materialId: Int) =
        apiService.getMaterial(token, materialId)

    suspend fun getQuiz(token: String, quizId: Int) =
        apiService.getQuiz(token, quizId).questions

    suspend fun getMultipleChoiceQuestion(token: String, question_id: Int) =
        apiService.getMultipleChoiceQuestion(token, question_id)

    suspend fun getArrangeWordsQuestion(token: String, question_id: Int) =
        apiService.getArrangeWordsQuestion(token, question_id)

    suspend fun getShortAnswerQuestion(token: String, question_id: Int) =
        apiService.getShortAnswerQuestion(token, question_id)

    suspend fun sendAnswer(token: String, listAnswer: List<Answer>) =
        apiService.sendAnswer(token, PostAnswerBody(listAnswer))
}