package com.tardigrade.capstonebangkit.view.child.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.tardigrade.capstonebangkit.adapter.LessonAdapter
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>()
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val lessonList = arrayListOf(
                Lesson(
                    coverImageUrl = "https://picsum.photos/200/300",
                    title = "Lesson 1",
                    type = "Grammar"
                ),
                Lesson(
                    coverImageUrl = "https://picsum.photos/200/300",
                    title = "Lesson 2",
                    type = "Vocabulary"
                ),
                Lesson(
                    coverImageUrl = "https://picsum.photos/200/300",
                    title = "Lesson 3",
                    type = "Vocabulary"
                ),
                Lesson(
                    coverImageUrl = "https://picsum.photos/200/300",
                    title = "Lesson 4",
                    type = "Grammar"
                ),
                Lesson(
                    coverImageUrl = "https://picsum.photos/200/300",
                    title = "Lesson 5",
                    type = "Vocabulary"
                )
            )

            newLessonsList.layoutManager = GridLayoutManager(context, 3)
            newLessonsList.adapter = LessonAdapter(lessonList)

            pastLessonsList.layoutManager = GridLayoutManager(context, 3)
            pastLessonsList.adapter = LessonAdapter(lessonList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}