package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.databinding.FragmentMultipleChoiceQuizBinding

class MultipleChoiceQuizFragment : Fragment() {
    private val viewModel by viewModels<MultipleChoiceQuizViewModel>()
    private val quizViewModel: QuizViewModel by activityViewModels()
    private var binding: FragmentMultipleChoiceQuizBinding? = null

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
            setQuizContent(it)
        }

        val multipleChoiceQuestion = MultipleChoiceQuestion(
            order = 0,
            type = 0,
            qText = "Test Question",
            qAudio = null,
            qImage = "https://picsum.photos/200/300",
            answer = "answer",
            choices = listOf(
                Choice(
                    choiceName = "A",
                    choiceText = "ChoiceChoice A",
                    choiceAudio = null,
                    choiceImage = "https://picsum.photos/200/300"
                ),
                Choice(
                    choiceName = "B",
                    choiceText = "ChoiceChoice B",
                    choiceAudio = null,
                    choiceImage = "https://picsum.photos/200/300"
                ),
                Choice(
                    choiceName = "C",
                    choiceText = "ChoiceChoice C",
                    choiceAudio = null,
                    choiceImage = "https://picsum.photos/200/300"
                ),
                Choice(
                    choiceName = "D",
                    choiceText = "ChoiceChoice D",
                    choiceAudio = null,
                    choiceImage = "https://picsum.photos/200/300"
                )
            )
        )

        viewModel.multipleChoiceQuestion.value = multipleChoiceQuestion

        binding?.apply {
            val quizFragment: QuizFragment = this@MultipleChoiceQuizFragment.parentFragment as QuizFragment
            btnNext.setOnClickListener { quizFragment.nextQuestion() }
            btnPrevious.setOnClickListener { quizFragment.previousQuestion() }
            btnBack.setOnClickListener { quizFragment.backToHome() }
        }
    }

    private fun setQuizContent(content: MultipleChoiceQuestion) {

        // Set the quiz question
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

        // Set the quiz choices
        binding?.rvChoices?.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            val choicesAdapter = MultipleChoiceAdapter(ArrayList(content.choices))
            adapter = choicesAdapter
        }
    }

    private fun nextQuestion() {
        TODO("not yet implemented")
    }

    private fun previousSlide() {
        TODO("not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}