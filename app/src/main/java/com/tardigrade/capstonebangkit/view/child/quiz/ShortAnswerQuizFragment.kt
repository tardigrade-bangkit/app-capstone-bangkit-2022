package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.databinding.FragmentMultipleChoiceQuizBinding
import com.tardigrade.capstonebangkit.databinding.FragmentShortAnswerQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar

class ShortAnswerQuizFragment : Fragment() {
    private val viewModel by viewModels<ShortAnswerQuizViewModel>()
    private var binding: FragmentShortAnswerQuizBinding? = null
    private var quizFragment: QuizFragment = this.parentFragment as QuizFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShortAnswerQuizBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.shortAnswerQuestion.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    setQuizContent(it.data)
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getShortAnswerQuestion(quizFragment.viewModel.currentQuizContent.value?.shortAnswerId!!)
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    TODO("not yet implemented")
                }
            }
        }

        viewModel.getShortAnswerQuestion(quizFragment.viewModel.currentQuizContent.value?.shortAnswerId!!)

        binding?.apply {
            btnNext.setOnClickListener { quizFragment.nextQuestion() }
            btnPrevious.setOnClickListener { quizFragment.previousQuestion() }
            btnBack.setOnClickListener { quizFragment.backToHome() }
        }
    }

    private fun setQuizContent(content: ShortAnswerQuestion) {

        content.qText?.let { binding?.tvQuestion?.text = it }
        content.qImage?.let {
            Glide.with(this@ShortAnswerQuizFragment)
                .asBitmap()
                .load(content.qImage)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding?.imgQuestion?.setImageBitmap(resource)
                    }
                })
        }

        if (content.type > 0) {
            binding?.apply {
                cardWarning.visibility = View.VISIBLE
                btnAction.visibility = View.VISIBLE
                edtAnswer.visibility = View.GONE
                btnNext.visibility = View.GONE

                when(content.type) {
                    1 -> {
                        tvWarning.text = getString(R.string.tv_warning_text_photo)
                        imgWarning.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.warning_photo))
                        btnAction.text = getString(R.string.btn_action_text_photo)
                    }
                    2 -> {
                        tvWarning.text = getString(R.string.tv_warning_text_audio)
                        imgWarning.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.warning_audio))
                        btnAction.text = getString(R.string.btn_action_text_audio)
                    }
                }
            }
        }

        binding?.apply{
            with(quizFragment.lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(currentLesson?.title).append(" - ")
                    .append(currentLessonContent?.title).append(" - ")
                    .append(content.order)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}