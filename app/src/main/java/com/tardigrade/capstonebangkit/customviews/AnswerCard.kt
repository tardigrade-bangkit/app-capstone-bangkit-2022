package com.tardigrade.capstonebangkit.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class AnswerCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var answer_text: String? = null
    var answer_image_url: String? = null
    var answer_audio_url: String? = null
    var answer_name: String = "A"

    // TODO: implement logic for variations of answer cards
}