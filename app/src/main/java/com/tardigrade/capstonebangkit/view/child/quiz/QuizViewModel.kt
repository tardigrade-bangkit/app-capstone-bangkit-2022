package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.QuizContent

class QuizViewModel : ViewModel() {
    private val _listQuizContent = MutableLiveData<List<QuizContent>>()
    val listQuizContent: MutableLiveData<List<QuizContent>> = _listQuizContent

    private val _currentQuizContent = MutableLiveData<QuizContent>()
    val currentQuizContent: MutableLiveData<QuizContent> = _currentQuizContent
}