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
    @field:SerializedName("type")
    val type: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("order")
    val order: Int,

    @field:SerializedName("quizzes_id")
    val quizzesId: Int? = null,

    @field:SerializedName("materials_id")
    val materialId: Int? = null,
)

data class MaterialContent(
    @field:SerializedName("order")
    val order: Int,

    @field:SerializedName("image")
    var imageUrl: String? = null,

    @field:SerializedName("audio")
    var audioUrl: String? = null,

    @field:SerializedName("type")
    val type: Int
)

data class QuizContent(
    @field:SerializedName("order")
    val order: Int,

    @field:SerializedName("type")
    val type: Int,

    @field:SerializedName("multiple_choice_id")
    val multipleChoiceId: Int? = null,

    @field:SerializedName("arrange_words_id")
    val arrangeWordsId: Int? = null,

    @field:SerializedName("short_answer_id")
    val shortAnswerId: Int? = null
)

data class MultipleChoiceQuestion(
    @field:SerializedName("order")
    val order: Int,

    @field:SerializedName("type")
    val type: Int,

    @field:SerializedName("question_text")
    val qText: String? = null,

    @field:SerializedName("question_image")
    val qImage: String? = null,

    @field:SerializedName("question_audio")
    val qAudio: String? = null,

    @field:SerializedName("answer")
    val answer: String,

    @field:SerializedName("answer_choices")
    val choices: List<Choice>
)

data class Choice(
    @field:SerializedName("choice")
    val choiceName: String,

    @field:SerializedName("image")
    val choiceImage: String? = null,

    @field:SerializedName("audio")
    val choiceAudio: String? = null,

    @field:SerializedName("text")
    val choiceText: String? = null
)

data class ArrangeWordsQuestion(
    @field:SerializedName("order")
    val order: Int,

    @field:SerializedName("type")
    val type: Int,

    @field:SerializedName("question_text")
    val qText: String? = null,

    @field:SerializedName("question_image")
    val qImage: String? = null,

    @field:SerializedName("question_audio")
    val qAudio: String? = null,

    @field:SerializedName("answer")
    val answer: String,

    @field:SerializedName("answer_words")
    val words: List<String>
)

data class ShortAnswerQuestion(
    @field:SerializedName("order")
    val order: Int,

    @field:SerializedName("type")
    val type: Int,

    @field:SerializedName("question_text")
    val qText: String? = null,

    @field:SerializedName("question_image")
    val qImage: String? = null,

    @field:SerializedName("question_audio")
    val qAudio: String? = null,

    @field:SerializedName("answer")
    val answer: String,
)
