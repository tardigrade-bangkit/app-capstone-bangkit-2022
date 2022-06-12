package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.ArrangeWordsQuestion

class ArrangeWordsQuizViewModel : ViewModel() {
    private val _arrangeWordQuestion = MutableLiveData<ArrangeWordsQuestion>()
    val arrangeWordQuestion: MutableLiveData<ArrangeWordsQuestion> = _arrangeWordQuestion

    private val _selectedWords = MutableLiveData<ArrayList<String>>()
    val selectedWords: MutableLiveData<ArrayList<String>> = _selectedWords

    private val _unselectedWords = MutableLiveData<ArrayList<String>>()
    val unselectedWords: MutableLiveData<ArrayList<String>> = _unselectedWords

    fun getSentence() : String {
        var sentence = ""
        selectedWords.value?.let {
            for (word in it) {
                sentence += word
                if (it.last().toString() != word) sentence += ' '
            }
        }
        return sentence
    }
}