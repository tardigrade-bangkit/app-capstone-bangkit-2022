package com.tardigrade.capstonebangkit.data.api

import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("users")
    suspend fun register(
        @Body newUser: NewUser
    ): GenericResponse

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body loginData: LoginData
    ): LoginResponse

    @Headers("Content-Type: application/json")
    @POST("pin")
    suspend fun addPin(
        @Header("x-access-token") token: String,
        @Body pinData: PinData
    ) : GenericResponse

    @Headers("Content-Type: application/json")
    @POST("pin/check")
    suspend fun checkPin(
        @Header("x-access-token") token: String,
        @Body pinData: PinData
    ) : GenericResponse

    @GET("avatars")
    suspend fun getAvatars() : GetAvatarsResponse

    @GET("children")
    suspend fun getChildren(
        @Header("x-access-token") token: String
    )  : GetChildrenResponse

    @GET("users/self")
    suspend fun getSelf(
        @Header("x-access-token") token: String
    ) : GetSelfResponse

    @Headers("Content-Type: application/json")
    @POST("children")
    suspend fun addChildren(
        @Header("x-access-token") token: String,
        @Body newChild: AddChild
    ): GenericResponse

    @GET("progress/{child_id}")
    suspend fun getChildrenLesson(
        @Header("x-access-token") token: String,
        @Path("child_id") childId: Int
    ) : GetProgressResponse

    @GET("badges/{child_id}")
    suspend fun getChildrenBadges(
        @Header("x-access-token") token: String,
        @Path("child_id") childId: Int
    ) : GetBadgesResponse

    @GET("achievements/{child_id}")
    suspend fun getChildrenAchievements(
        @Header("x-access-token") token: String,
        @Path("child_id") childId: Int
    ) : GetAchievementsResponse

    @GET("usages/{child_id}")
    suspend fun getChildrenUsages(
        @Header("x-access-token") token: String,
        @Path("child_id") childId: Int
    ) : GetUsagesResponse

    @Headers("Content-Type: application/json")
    @POST("usages/{child_id}")
    suspend fun addUsage(
        @Header("x-access-token") token: String,
        @Body addUsageData: AddUsageData,
        @Path("child_id") childId: Int
    ): GenericResponse

    @Headers("Content-Type: application/json")
    @POST("usages/end/{child_id}")
    suspend fun addUsage(
        @Header("x-access-token") token: String,
        @Body addEndUsageData: AddEndUsageData,
        @Path("child_id") childId: Int
    ): GenericResponse

    @GET("lessons/{level}")
    suspend fun getLessonsByLevel(
        @Header("x-access-token") token: String,
        @Path("level") level: Int
    ): GetLessonsResponse

    @GET("lessons/content/{lesson_id}")
    suspend fun getLesson(
        @Header("x-access-token") token: String,
        @Path("lesson_id") lesson_id: Int
    ): GetLessonResponse

    @GET("materials/{material_id}")
    suspend fun getMaterial(
        @Header("x-access-token") token: String,
        @Path("material_id") material_id: Int
    ): GetMaterialResponse

    @GET("quizzes/{quiz_id}")
    suspend fun getQuiz(
        @Header("x-access-token") token: String,
        @Path("quiz_id") quiz_id: Int
    ): GetQuizResponse

    @GET("questions/{question_id}")
    suspend fun getMultipleChoiceQuestion(
        @Header("x-access-token") token: String,
        @Path("question_id") question_id: Int
    ): MultipleChoiceQuestion

    @GET("questions/{question_id}")
    suspend fun getArrangeWordsQuestion(
        @Header("x-access-token") token: String,
        @Path("question_id") question_id: Int
    ): GetArrangeWordsResponse

    @GET("questions/{question_id}")
    suspend fun getShortAnswerQuestion(
        @Header("x-access-token") token: String,
        @Path("question_id") question_id: Int
    ): GetShortAnswerResponse

    @Headers("Content-Type: application/json")
    @POST("quiz")
    suspend fun sendAnswer(
        @Header("x-access-token") token: String,
        @Body answer: PostAnswerBody
    ): PostAnswerResponse
}