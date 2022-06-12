package com.tardigrade.capstonebangkit.data.repository

import com.tardigrade.capstonebangkit.data.api.ApiService

class LessonRepository(private val apiService: ApiService) {
    suspend fun getLessonsByLevel(token: String, level: Int) =
        apiService.getLessonsByLevel(token, level).lessons

    suspend fun getLesson(token: String, lessonId: Int) =
        apiService.getLesson(token, lessonId).lessonContents

    suspend fun getMaterial(token: String, materialId: Int) =
        apiService.getMaterial(token, materialId).materialContents

    suspend fun getQuiz(token: String, quizId: Int) =
        apiService.getQuiz(token, quizId).questions

    suspend fun getMultipleChoiceQuestion(token: String, question_id: Int) =
        apiService.getMultipleChoiceQuestion(token, question_id)
}