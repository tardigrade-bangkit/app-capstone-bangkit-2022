package com.tardigrade.capstonebangkit.customviews

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.ViewLessonCardBinding

class LessonCard : FrameLayout{
    private val binding = ViewLessonCardBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addView(binding.root)
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
        binding.root.clipToOutline = true

        // Set title color based on image color
        val palette = Palette.from(image).generate()
        binding.lessonTitle.background.setTint(
            palette.getLightVibrantColor(
                palette.getLightMutedColor(ContextCompat.getColor(context, R.color.blue_100))))
        binding.lessonTitle.setTextColor(
            palette.getDarkMutedColor(
                palette.getDarkVibrantColor(ContextCompat.getColor(context, R.color.blue_800))))
    }
}
