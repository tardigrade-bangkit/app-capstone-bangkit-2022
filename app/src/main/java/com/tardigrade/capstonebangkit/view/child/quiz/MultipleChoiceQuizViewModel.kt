package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion

class MultipleChoiceQuizViewModel : ViewModel() {
    private val _multipleChoiceQuestion = MutableLiveData<MultipleChoiceQuestion>()
    val multipleChoiceQuestion: MutableLiveData<MultipleChoiceQuestion> = _multipleChoiceQuestion
}