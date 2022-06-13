package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.databinding.FragmentQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel

class QuizFragment : Fragment() {
    val viewModel by viewModels<QuizViewModel>() {
        QuizViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    val lessonContentViewModel: LessonContentViewModel by activityViewModels()

    private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
    private val lessonRepository = LessonRepository(ApiConfig.getApiService())
    private var binding: FragmentQuizBinding? = null
    private var listQuizContent: List<QuizContent>? = null

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
            if (it != null) {
                setQuizContent(it)
            }
        }

        viewModel.listQuizContent.observe(viewLifecycleOwner) { contents ->
            when(contents) {
                is Result.Success -> {
                    viewModel.setCurrentQuizContent(contents.data[0])
                    listQuizContent = contents.data
                }
                is Result.Error -> {
                    val error = contents.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(com.tardigrade.capstonebangkit.R.string.try_again)) {
                                lessonContentViewModel.currentLessonContent?.quizzesId?.let { viewModel.getQuiz(it) }
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    TODO("not yet implemented")
                }
            }
        }

        lessonContentViewModel.currentLessonContent?.quizzesId?.let { viewModel.getQuiz(it) }
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
        val currentIndex = listQuizContent?.indexOf(viewModel.currentQuizContent.value)
        if (currentIndex != null && currentIndex < (listQuizContent?.size?.minus(1) ?: 0)) {
            viewModel.setCurrentQuizContent(listQuizContent?.get(currentIndex + 1))
        } else {
            lessonContentViewModel.getNextLessonContent()
            val nextLessonContent = lessonContentViewModel.currentLessonContent
            if (nextLessonContent == null) backToHome()
            when (nextLessonContent?.type) {
                1 -> findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_materialFragment_self)
                2 -> findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_materialFragment_to_quizFragment)
            }
        }
    }

    fun previousQuestion() {
        val currentIndex = listQuizContent?.indexOf(viewModel.currentQuizContent.value)
        if (currentIndex != null && currentIndex > 0) {
            viewModel.setCurrentQuizContent(listQuizContent?.get(currentIndex - 1))
        } else {
            lessonContentViewModel.getPreviousContent()
            val prevLessonContent = lessonContentViewModel.currentLessonContent
            if (prevLessonContent == null) backToHome()
            when (prevLessonContent?.type) {
                1 -> findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_materialFragment_self)
                2 -> findNavController().navigate(com.tardigrade.capstonebangkit.R.id.action_materialFragment_to_quizFragment)
            }
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