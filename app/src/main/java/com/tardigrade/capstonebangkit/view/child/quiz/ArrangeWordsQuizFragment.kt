package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.data.model.ArrangeWordsQuestion
import com.tardigrade.capstonebangkit.databinding.FragmentArrangeWordsQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.child.material.MaterialViewModel

class ArrangeWordsQuizFragment : Fragment(), MediaPlayer.OnPreparedListener {
    private val viewModel by viewModels<ArrangeWordsQuizViewModel>() {
        ArrangeWordsQuizViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    private val quizViewModel : QuizViewModel by activityViewModels()
    val mediaPlayer: MediaPlayer = MediaPlayer()
    var answer: String? = null
    var arrangeWordsId: Int? = null

    private var binding: FragmentArrangeWordsQuizBinding? = null
    private var quizFragment: QuizFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArrangeWordsQuizBinding.inflate(inflater, container, false)
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

        quizFragment = this.parentFragment as QuizFragment

        viewModel.arrangeWordsQuestion.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    setQuizContent(it.data)
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getArrangeWordsQuestion(quizViewModel.quizContent?.arrangeWordsId!!)
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    TODO("not yet implemented")
                }
            }
        }

        viewModel.selectedWords.observe(viewLifecycleOwner) {
            val sentence = viewModel.getSentence()
            answer = sentence
        }

        arrangeWordsId = quizViewModel.quizContent?.arrangeWordsId!!
        viewModel.getArrangeWordsQuestion(arrangeWordsId!!)

        binding?.apply {
            btnNext.setOnClickListener {
                quizFragment?.nextQuestion()
                answer?.let { it1 -> quizViewModel.setAnswer(arrangeWordsId!!, it1) }
            }
            btnPrevious.setOnClickListener {
                quizFragment?.previousQuestion()
                answer?.let { it1 -> quizViewModel.setAnswer(arrangeWordsId!!, it1) }
            }
            btnBack.setOnClickListener { quizFragment?.backToHome() }
        }
    }

    private fun setQuizContent(content: ArrangeWordsQuestion) {

        // Set the quiz question
        content.qText?.let { binding?.tvQuestion?.text = it }
        content.qImage?.let {
            Glide.with(this@ArrangeWordsQuizFragment)
                .asBitmap()
                .load(content.qImage)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding?.imgQuestion?.setImageBitmap(resource)
                    }
                })
        }

        val allWords = ArrayList<String>()
        viewModel.selectedWords.value = allWords

        // Set the quiz choices
        binding?.cgWords?.apply {
            for (word in content.words) {
                addView(newAnswerChip(word))
            }
        }

        binding?.apply{
            with(quizFragment?.lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(this?.currentLesson?.title ?: "").append(" - ")
                    .append(this?.currentLessonContent?.title ?: "").append(" - ")
                    .append(content.order)
            }
        }
    }

    fun prepareAudio(audioUrl: String) {
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

    private fun newAnswerChip(text: String) : Chip {
        val answerChip = Chip(activity)
        answerChip.apply {
            isSelected = false
            this.text = text
            setChipBackgroundColorResource(
                if (isSelected) R.color.blue_100
                else R.color.blue_400
            )
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setOnClickListener{ moveWordChip(this) }
        }
        return answerChip
    }

   private fun moveWordChip(chip: Chip) {
       if (!chip.isSelected) {
           chip.isSelected = true
           chip.setChipBackgroundColorResource(R.color.blue_100)
           chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
           binding?.apply {
               cgWords.removeView(chip)
               cgSelectedWords.addView(chip)
           }
           val selectedWords = viewModel.selectedWords.value as ArrayList<String>
           selectedWords.add(chip.text.toString())
           viewModel.selectedWords.value = selectedWords

       } else {
           chip.isSelected = false
           chip.setChipBackgroundColorResource(R.color.blue_400)
           chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
           binding?.apply {
               cgSelectedWords.removeView(chip)
               cgWords.addView(chip)
           }
           val selectedWords = viewModel.selectedWords.value as ArrayList<String>
           selectedWords.removeAt(selectedWords.indexOf(chip.text.toString()))
           viewModel.selectedWords.value = selectedWords
       }
   }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}