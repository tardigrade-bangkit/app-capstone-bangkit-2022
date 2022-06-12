package com.tardigrade.capstonebangkit.view.child.quiz

import android.R
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.databinding.FragmentQuizBinding
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel


class QuizFragment : Fragment() {
    private val viewModel: QuizViewModel by activityViewModels()
    private val lessonContentViewModel: LessonContentViewModel by activityViewModels()
    private var binding: FragmentQuizBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentQuizContent.observe(viewLifecycleOwner) {
            setQuizContent(it)
        }

        viewModel.listQuizContent.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.currentQuizContent.value = it[0]
            }
        }

        val quizContents = arrayListOf(
            QuizContent(
                order = 1,
                type = 0,
                multipleChoiceId = 1
            ),
            QuizContent(
                order = 2,
                type = 1,
                arrangeWordsId = 1
            ),
            QuizContent(
                order = 3,
                type = 2,
                shortAnswerId = 1
            ),
        )

        viewModel.listQuizContent.value = quizContents
    }

    private fun setQuizContent(content: QuizContent) {
        when(content.type) {
            0 -> {
                childFragmentManager.commit {
                    replace<MultipleChoiceQuizFragment>(binding!!.quizContentFragment.id)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
            1 -> {
                childFragmentManager.commit {
                    replace<ArrangeWordsQuizFragment>(binding!!.quizContentFragment.id)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

    fun nextQuestion() {
        val currentIndex = viewModel.listQuizContent.value?.indexOf(viewModel.currentQuizContent.value)
        if (currentIndex != null && currentIndex < viewModel.listQuizContent.value?.size!! - 1) {
            viewModel.currentQuizContent.value = viewModel.listQuizContent.value?.get(currentIndex + 1)
        } else {
//            val nextLessonContent = lessonContentViewModel.getNextLessonContent()
//            if (nextLessonContent == null) findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_quizFragment_to_homeFragment)
//            when (nextLessonContent.type) {
//                0 -> findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_quizFragment_to_materialFragment)
//                1 -> findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_quizFragment_self)
//            }
        }
    }

    fun previousQuestion() {
    val currentIndex = viewModel.listQuizContent.value?.indexOf(viewModel.currentQuizContent.value)
        if (currentIndex != null && currentIndex > 0) {
            viewModel.currentQuizContent.value = viewModel.listQuizContent.value?.get(currentIndex - 1)
        }
    }

    fun backToHome() {
        findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_quizFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}