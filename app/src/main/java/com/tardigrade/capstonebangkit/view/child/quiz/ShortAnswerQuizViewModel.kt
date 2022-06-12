package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.ShortAnswerQuestion

class ShortAnswerQuizViewModel : ViewModel() {
    private val _shortAnswerQuestion = MutableLiveData<ShortAnswerQuestion>()
    val shortAnswerQuestion: MutableLiveData<ShortAnswerQuestion> = _shortAnswerQuestion
}