package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.LessonAdapter
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.customviews.AnswerCard
import com.tardigrade.capstonebangkit.customviews.LessonCard
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import com.tardigrade.capstonebangkit.databinding.FragmentMultipleChoiceQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar

class MultipleChoiceQuizFragment : Fragment() {
    private val viewModel by viewModels<MultipleChoiceQuizViewModel>() {
        MultipleChoiceQuizViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6N30.tbrCFKYdTTrxgl5hSQFld2ErZhUjh8OicSkJ62z_rww"
//            requireContext().preferences.getToken()
//                ?: error("must have token")
        )
    }
    private val quizViewModel : QuizViewModel by activityViewModels()
    var answer : String? = null
    var multipleChoiceId : Int? = null
    private var binding: FragmentMultipleChoiceQuizBinding? = null
    private var quizFragment: QuizFragment? = null
    private var qAudioUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMultipleChoiceQuizBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizFragment = this.parentFragment as QuizFragment

        viewModel.multipleChoiceQuestion.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    Log.d("multipleChoiceQuestion", it.data.toString())
                    setQuizContent(it.data)
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getMultipleChoiceQuestion(quizViewModel.currentQuizContent.value?.multipleChoiceId!!)
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    TODO("not yet implemented")
                }
            }
        }

        multipleChoiceId = quizViewModel.currentQuizContent.value?.multipleChoiceId!!
        Log.d("currentQuizContent", quizViewModel.quizContent.toString())
        viewModel.getMultipleChoiceQuestion(multipleChoiceId!!)

        binding?.apply {
            btnNext.setOnClickListener {
                answer?.let { it1 -> quizViewModel.setAnswer(multipleChoiceId!!, it1) }
                quizFragment?.nextQuestion()
            }
            btnNext.visibility = View.GONE
            btnPrevious.visibility = View.GONE
            btnPrevious.setOnClickListener {
                answer?.let { it1 -> quizViewModel.setAnswer(multipleChoiceId!!, it1) }
                quizFragment?.previousQuestion()
            }
            btnBack.setOnClickListener { quizFragment?.backToHome() }
            btnReplay.setOnClickListener {
                quizFragment?.mediaPlayer?.stop()
                quizFragment?.mediaPlayer?.reset()
                qAudioUrl?.let { it -> quizFragment?.prepareAudio(it) }
            }
        }
    }

    private fun setQuizContent(content: MultipleChoiceQuestion) {

        content.qText?.let { binding?.tvQuestion?.text = it }
        content.qImage?.let {
            Glide.with(this@MultipleChoiceQuizFragment)
                .asBitmap()
                .load(content.qImage)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding?.imgQuestion?.setImageBitmap(resource)
                    }
                })
        }

        binding?.apply{
            with(quizFragment?.lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(this?.currentLesson?.title ?: "").append(" - ")
                    .append(this?.currentLessonContent?.title ?: "").append(" - ")
                    .append(content.order)
            }
        }

        binding?.rvChoices?.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            val choicesAdapter = MultipleChoiceAdapter(ArrayList(content.choices))
            adapter = choicesAdapter

            choicesAdapter.setOnItemClickCallback(object : MultipleChoiceAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Choice, view: AnswerCard) {
                    quizFragment?.mediaPlayer?.stop()
                    quizFragment?.mediaPlayer?.reset()
                    answer = data.choiceName
                    data.choiceAudio?.let { quizFragment?.prepareAudio(it) }
                    binding?.btnNext?.visibility = View.VISIBLE
                    binding?.btnPrevious?.visibility = View.VISIBLE
                }
            })
        }

        content.qAudio?.let {
            qAudioUrl = it
            quizFragment?.prepareAudio(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}