package com.tardigrade.capstonebangkit.view.parent.placementquiz

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.data.model.Choice
import com.tardigrade.capstonebangkit.data.model.MultipleChoiceQuestion
import com.tardigrade.capstonebangkit.databinding.FragmentPlacementQuizBinding
import com.tardigrade.capstonebangkit.databinding.FragmentRegisterBinding
import com.tardigrade.capstonebangkit.misc.MarginItemDecoration
import com.tardigrade.capstonebangkit.misc.VerticalSpacingItemDecoration
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.loadImage
import com.tardigrade.capstonebangkit.utils.showSnackbar

class PlacementQuizFragment : Fragment() {
    private val viewModel by viewModels<PlacementQuizViewModel>()
    private var binding: FragmentPlacementQuizBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacementQuizBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getActionBar(requireActivity())?.apply {
            show()
            title = getString(R.string.placement_quiz)
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

        binding?.apply {
            choiceList.addItemDecoration(VerticalSpacingItemDecoration(24))

            nextQuestionBtn.setOnClickListener {
                showSnackbar(binding?.root ?: return@setOnClickListener, (choiceList.adapter as MultipleChoiceAdapter).getSelectedItem().toString())
            }
        }

        setupQuestions(multipleChoiceQuestion)
    }

    fun setupQuestions(multiQuestion: MultipleChoiceQuestion) {
        binding?.apply {
            if (multiQuestion.qText != null) {
                question.text = multiQuestion.qText
            } else {
                question.visibility = View.GONE
            }

            if (multiQuestion.qImage != null) {
                questionImage.loadImage(multiQuestion.qImage)
            } else {
                questionImage.visibility = View.GONE
            }

            choiceList.adapter = MultipleChoiceAdapter(ArrayList(multiQuestion.choices))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}