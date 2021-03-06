package com.tardigrade.capstonebangkit.customviews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.ViewAnswerCardBinding

class AnswerCard : LinearLayout {
    private val binding = ViewAnswerCardBinding.inflate(LayoutInflater.from(context))

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addView(binding.root)
        binding.apply {
            answerAudio.visibility = GONE
            answerImage.visibility = GONE
            answerText.visibility = GONE
        }

        binding.root.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    fun setAnswerContent(
        answer_name: String = "A",
        answer_text: String? = null,
        answer_image_url: String? = null,
        answer_audio_url: String? = null
    ) {

        if (answer_text != null) {
            binding.answerText.apply {
                text = answer_text
                visibility = VISIBLE
            }
        }

        if (answer_image_url != null) {
            binding.answerImage.apply {
                Glide.with(context)
                    .asBitmap()
                    .load(answer_image_url)
                    .into(object: CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {}

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            setImageBitmap(resource)
                        }
                    })
                visibility = VISIBLE
            }
        }

        if (answer_audio_url != null) {
            binding.answerAudio.apply {
                visibility = VISIBLE
            }
        }

        binding.choiceName.text = answer_name
    }

    override fun setSelected(selected: Boolean) {
        if (selected) {
            binding.root.background = ContextCompat.getDrawable(context, R.drawable.answer_selected)
        } else {
            binding.root.background = ContextCompat.getDrawable(context, R.drawable.answer_unselected)
        }
    }
}