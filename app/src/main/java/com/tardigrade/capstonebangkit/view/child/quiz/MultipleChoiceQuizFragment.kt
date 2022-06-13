package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import com.tardigrade.capstonebangkit.databinding.FragmentMultipleChoiceQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar

class MultipleChoiceQuizFragment : Fragment() {
    private val viewModel by viewModels<MultipleChoiceQuizViewModel>()
    private var binding: FragmentMultipleChoiceQuizBinding? = null
    private var quizFragment: QuizFragment = this.parentFragment as QuizFragment

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

        viewModel.multipleChoiceQuestion.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    setQuizContent(it.data)
                }
                is Result.Error -> {
                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error, getString(R.string.try_again)) {
                                viewModel.getMultipleChoiceQuestion(quizFragment.viewModel.currentQuizContent.value?.multipleChoiceId!!)
                            }
                        }
                    }
                }
                is Result.Loading -> {
                    TODO("not yet implemented")
                }
            }
        }

        viewModel.getMultipleChoiceQuestion(quizFragment.viewModel.currentQuizContent.value?.multipleChoiceId!!)

        binding?.apply {
            btnNext.setOnClickListener { quizFragment.nextQuestion() }
            btnPrevious.setOnClickListener { quizFragment.previousQuestion() }
            btnBack.setOnClickListener { quizFragment.backToHome() }
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
            with(quizFragment.lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(currentLesson?.title).append(" - ")
                    .append(currentLessonContent?.title).append(" - ")
                    .append(content.order)
            }
        }

        binding?.rvChoices?.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            val choicesAdapter = MultipleChoiceAdapter(ArrayList(content.choices))
            adapter = choicesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}