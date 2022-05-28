package com.tardigrade.capstonebangkit.customviews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.ViewLessonCardBinding

class LessonCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    private val binding = ViewLessonCardBinding.inflate(LayoutInflater.from(context))

    init {
        inflate(context, R.layout.view_lesson_card, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.LessonCard)
        setLessonCoverImage(
            BitmapFactory.decodeResource(
                resources,
                attributes.getResourceId(R.styleable.LessonCard_cover_image, 0)
            )
        )
        setLessonTitle(attributes.getString(R.styleable.LessonCard_lesson_title) ?: "Lesson Title")
        setLessonType(attributes.getString(R.styleable.LessonCard_lesson_type) ?: "Lesson Type")
        clipToOutline = true
        attributes.recycle()
    }

    fun setLessonTitle(title: String) {
        binding.lessonTitle.text = title
    }

    fun setLessonType(type: String) {
        with(binding.lessonType) {
            when (type) {
                "Grammar" -> {
                    setTextColor(ContextCompat.getColor(context, R.color.blue_100))
                    background.setTint(ContextCompat.getColor(context, R.color.blue_800))
                }
                "Vocabulary" -> {
                    setTextColor(ContextCompat.getColor(context, R.color.red_100))
                    background.setTint(ContextCompat.getColor(context, R.color.red_800))
                }
                else -> {
                    setTextColor(ContextCompat.getColor(context, R.color.green_100))
                    background.setTint(ContextCompat.getColor(context, R.color.green_800))
                }
            }
            text = type
        }
    }

    fun setLessonCoverImage(image: Bitmap) {
        binding.lessonCoverImage.setImageBitmap(image)

        // Set title color based on image color
        val palette = Palette.from(image).generate()
        binding.lessonTitle.background.setTint(
            palette.getLightVibrantColor(ContextCompat.getColor(context, R.color.blue_800)))
        binding.lessonTitle.setTextColor(
            palette.getDarkMutedColor(ContextCompat.getColor(context, R.color.blue_200)))
    }
}
