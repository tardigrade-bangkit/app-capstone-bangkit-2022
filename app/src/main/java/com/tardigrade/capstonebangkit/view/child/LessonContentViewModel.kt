package com.tardigrade.capstonebangkit.view.child

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tardigrade.capstonebangkit.data.model.LessonContent

class LessonContentViewModel : ViewModel() {
    private val _listLessonContent = MutableLiveData<List<LessonContent>>()
    val listLessonContent: MutableLiveData<List<LessonContent>> = _listLessonContent

    private var currentLessonContent = -1

    fun setListLessonContent(listLessonContent: List<LessonContent>) {
        _listLessonContent.value = listLessonContent
    }

    fun getNextLessonContent() : LessonContent {
        Log.d("getNextLessonContent", currentLessonContent.toString())
        return if (currentLessonContent == -1) {
            currentLessonContent = 0
            listLessonContent.value!![0]
        } else {
            currentLessonContent += 1
            listLessonContent.value!![currentLessonContent]
        }
    }
}