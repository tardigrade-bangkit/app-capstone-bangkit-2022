package com.tardigrade.capstonebangkit.view.child

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.LessonContent

class LessonContentViewModel : ViewModel() {
    private val _listLessonContent = MutableLiveData<List<LessonContent>>()
    val listLessonContent: MutableLiveData<List<LessonContent>> = _listLessonContent

    private var currentLessonContent: Int? = null

    fun setListLessonContent(listLessonContent: List<LessonContent>) {
        _listLessonContent.value = listLessonContent
    }

    fun getNextLessonContent() : LessonContent {
        return if (currentLessonContent == null) {
            currentLessonContent = 0
            listLessonContent.value!![0]
        } else {
            currentLessonContent = currentLessonContent!! + 1
            listLessonContent.value!![currentLessonContent!!]
        }
    }
}