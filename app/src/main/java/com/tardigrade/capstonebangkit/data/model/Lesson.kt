package com.tardigrade.capstonebangkit.data.model

data class Lesson(
    val coverImageUrl: String,
    val title: String,
    val type: String,
    var lessonContent: ArrayList<LessonContent>? = null
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
