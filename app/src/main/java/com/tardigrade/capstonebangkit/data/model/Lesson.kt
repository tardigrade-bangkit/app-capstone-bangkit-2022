package com.tardigrade.capstonebangkit.data.model

import com.google.gson.annotations.SerializedName

data class Lesson(
    @field:SerializedName("cover_image")
    val coverImageUrl: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("type")
    val type: String = "",

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("finished_date")
    val finishedDate: String? = null,
)

data class LessonContent(
    val title: String,
    val type: Int
)

data class MaterialContent(
    val order: Int,
    var imageUrl: String? = null,
    var audioUrl: String? = null,
)

data class QuizContent(
    val order: Int,
    val type: Int,
    val multipleChoiceId: Int? = null,
    val arrangeWordsId: Int? = null,
    val shortAnswerId: Int? = null
)

data class MultipleChoiceQuestion(
    val order: Int,
    val type: Int,
    val qText: String? = null,
    val qImage: String? = null,
    val qAudio: String? = null,
    val answer: String,
    val choices: List<Choice>
)

data class Choice(
    val choiceName: String,
    val choiceImage: String? = null,
    val choiceAudio: String? = null,
    val choiceText: String? = null
)

data class ArrangeWordsQuestion(
    val order: Int,
    val type: Int,
    val qText: String? = null,
    val qImage: String? = null,
    val qAudio: String? = null,
    val answer: String,
    val words: List<String>
)

data class ShortAnswerQuestion(
    val order: Int,
    val type: Int,
    val qText: String? = null,
    val qImage: String? = null,
    val qAudio: String? = null,
    val answer: String
)
