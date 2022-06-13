package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.QuizContent
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.databinding.FragmentQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel
import java.lang.Exception

class QuizFragment : Fragment(), MediaPlayer.OnPreparedListener {
    private val viewModel : QuizViewModel by activityViewModels()
    val mediaPlayer: MediaPlayer = MediaPlayer()
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

        mediaPlayer.apply {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            setAudioAttributes(attributes)
            setOnPreparedListener{onPrepared(this)}
        }

        viewModel.currentQuizContent.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("currentQuizContent", it.toString())
                setQuizContent(it)
            }
        }

        viewModel.listQuizContent.observe(viewLifecycleOwner) { contents ->
            when(contents) {
                is Result.Success -> {
                    viewModel.setCurrentQuizContent(contents.data[0])
                    Log.d("listQuizContent", contents.toString())
                    listQuizContent = contents.data
                }
                is Result.Error -> {
                    val error = contents.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(com.tardigrade.capstonebangkit.R.string.try_again)) {
                                lessonContentViewModel.currentLessonContent?.quizzesId?.let { viewModel.getQuiz(it, lessonRepository, token) }
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    TODO("not yet implemented")
                }
            }
        }

        lessonContentViewModel.currentLessonContent?.quizzesId?.let { viewModel.getQuiz(it, lessonRepository, token) }
    }

    private fun setQuizContent(content: QuizContent) {
        when(content.type) {
            0 -> {
                Log.d("setQuizContent", content.toString())
                try {
                    childFragmentManager.commit {
                        replace<MultipleChoiceQuizFragment>(binding!!.quizContentFragment.id)
                        setReorderingAllowed(true)
                    }
                } catch (e: InstantiationException) {
                    Log.d("setQuizContent", e.toString())
                }
            }
            1 -> {
                Log.d("setQuizContent", content.toString())
                try {
                    childFragmentManager.commit {
                        replace<ArrangeWordsQuizFragment>(binding!!.quizContentFragment.id)
                        setReorderingAllowed(true)
                    }
                } catch (e: InstantiationException) {
                    Log.d("setQuizContent", e.toString())
                }
            }
            2 -> {
                childFragmentManager.commit {
                    replace<ShortAnswerQuizFragment>(binding!!.quizContentFragment.id)
                    setReorderingAllowed(true)
                }
            }
        }
    }

    fun prepareAudio(audioUrl: String) {
        Log.d("prepareAudio", audioUrl)
        mediaPlayer.apply {
            setDataSource(audioUrl)
            setOnPreparedListener{onPrepared(this)}
            prepareAsync()
        }
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.d("onPrepared", "prepared")
        mediaPlayer.start()
    }

    fun nextQuestion() {
        mediaPlayer.stop()
        mediaPlayer.reset()

        val currentIndex = listQuizContent?.indexOf(viewModel.currentQuizContent.value)
        if (currentIndex != null && currentIndex < (listQuizContent?.size?.minus(1) ?: 0)) {
            viewModel.setCurrentQuizContent(listQuizContent?.get(currentIndex + 1))
        } else {
            lessonContentViewModel.getNextLessonContent()
            val nextLessonContent = lessonContentViewModel.currentLessonContent
            if (nextLessonContent == null) backToHome()
            with(nextLessonContent) {
                if (this?.materialId != null) findNavController().navigate(R.id.action_quizFragment_to_materialFragment)
                else if (this?.quizzesId != null) findNavController().navigate(R.id.action_quizFragment_self)
            }
        }
    }

    fun previousQuestion() {
        mediaPlayer.stop()
        mediaPlayer.reset()

        val currentIndex = listQuizContent?.indexOf(viewModel.currentQuizContent.value)
        if (currentIndex != null && currentIndex > 0) {
            viewModel.setCurrentQuizContent(listQuizContent?.get(currentIndex - 1))
        } else {
            lessonContentViewModel.getPreviousContent()
            val prevLessonContent = lessonContentViewModel.currentLessonContent
            if (prevLessonContent == null) backToHome()
            with(prevLessonContent) {
                if (this?.materialId != null) findNavController().navigate(R.id.action_quizFragment_to_materialFragment)
                else if (this?.quizzesId != null) findNavController().navigate(R.id.action_quizFragment_self)
            }
        }
    }

    fun backToHome() {
        findNavController().navigate(R.id.action_quizFragment_to_homeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}