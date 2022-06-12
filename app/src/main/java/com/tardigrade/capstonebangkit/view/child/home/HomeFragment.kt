package com.tardigrade.capstonebangkit.view.child.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.LessonAdapter
import com.tardigrade.capstonebangkit.customviews.LessonCard
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.data.model.LessonContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.databinding.FragmentHomeBinding
import com.tardigrade.capstonebangkit.misc.MarginItemDecoration
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>() {
        HomeViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    private val lessonContentViewModel by viewModels<LessonContentViewModel>() {
        LessonContentViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    private var binding: FragmentHomeBinding? = null
    private var listLessonContent: List<LessonContent>? = null

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

        viewModel.getLessons(1)

        viewModel.listLesson.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    Log.d("listLesson Success", it.data.toString())
                    setLessonData(it.data)
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getLessons(1)
                            }
                        }
                    }
                }
            }
        }

        lessonContentViewModel.listLessonContent.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    Log.d("lesson Success", it.data.toString())
                    listLessonContent = it.data
                    lessonContentViewModel.lessonContents = it.data
                    lessonContentViewModel.getNextLessonContent()
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getLessons(1)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setLessonData(lessons: List<Lesson>) {
        binding?.apply {
            newLessonsList.layoutManager = GridLayoutManager(context, 3)

            val newLessonListAdapter = LessonAdapter(lessons)
            newLessonsList.adapter = newLessonListAdapter
            newLessonListAdapter.setOnItemClickCallback(object : LessonAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Lesson?, view: LessonCard) {
                    data?.let { lessonContentViewModel.getLessonContent(it.id) }

                    when (lessonContentViewModel.currentLessonContent?.type) {
                        0 -> findNavController().navigate(R.id.action_homeFragment_to_materialFragment)
                        1 -> findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
                    }
                }
            })

            newLessonsList.addItemDecoration(MarginItemDecoration(24, 3, GridLayout.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}