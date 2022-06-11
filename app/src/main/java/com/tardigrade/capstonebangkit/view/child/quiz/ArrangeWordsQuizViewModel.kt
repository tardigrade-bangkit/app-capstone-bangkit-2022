package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.ArrangeWordsQuestion

class ArrangeWordQuizViewModel : ViewModel() {
    private val _arrangeWordQuestion = MutableLiveData<ArrangeWordsQuestion>()
    val arrangeWordQuestion: MutableLiveData<ArrangeWordsQuestion> = _arrangeWordQuestion

    private val _selectedWords = MutableLiveData<List<String>>()
    val selectedWords: MutableLiveData<List<String>> = _selectedWords

    private val _unselectedWords = MutableLiveData<List<String>>()
    val unselectedWords: MutableLiveData<List<String>> = _unselectedWords
}