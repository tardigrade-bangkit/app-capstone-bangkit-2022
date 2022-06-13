package com.tardigrade.capstonebangkit.view.parent.placementquiz

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.MultipleChoiceAdapter
import com.tardigrade.capstonebangkit.customviews.AnswerCard
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.api.PostAnswerResponse
import com.tardigrade.capstonebangkit.data.model.*
import com.tardigrade.capstonebangkit.data.repository.LessonRepository
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.databinding.FragmentPlacementQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.misc.VerticalSpacingItemDecoration
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.loadImage
import com.tardigrade.capstonebangkit.utils.setVisible
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.ChildActivity
import com.tardigrade.capstonebangkit.view.parent.login.preferences

class PlacementQuizFragment : Fragment() {
    private val viewModel by viewModels<PlacementQuizViewModel> {
        PlacementQuizViewModel.Factory(
            LessonRepository(ApiConfig.getApiService()),
            requireContext().preferences.getToken() ?: error("must have token"),
            ProfileRepository(ApiConfig.getApiService())
        )
    }
    private var binding: FragmentPlacementQuizBinding? = null

    private var chosenChild: ChildProfile? = null
    private var currentAnswer: String? = null

    private var isListQuestionLoading = false
    private var isQuestionLoading = false
    private var isSendAnswerLoading = false
    private var isUpdateChildLoading = false

    private val listQuestion = mutableListOf<QuizContent>()
    private val listAnswer = mutableListOf<Answer>()
    private var curQuestion = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacementQuizBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chosenChild = PlacementQuizFragmentArgs.fromBundle(arguments as Bundle).choosenChild

        getActionBar(requireActivity())?.apply {
            show()
            title = getString(R.string.placement_quiz)
        }

        showLoading()

        binding?.apply {
            choiceList.addItemDecoration(VerticalSpacingItemDecoration(24))

            nextQuestionBtn.setOnClickListener {
                if (currentAnswer != null) {
                    listAnswer.add(
                        Answer(
                            listQuestion[curQuestion].id,
                            currentAnswer as String
                        )
                    )

                    currentAnswer = null

                    if (curQuestion < listQuestion.size - 1) {
                        curQuestion++
                        viewModel.getQuestion(listQuestion[curQuestion])
                    } else {
                        viewModel.sendAnswer(listAnswer)
                    }
                } else if (curQuestion >= listQuestion.size - 1) {
                    viewModel.sendAnswer(listAnswer)
                }
            }
        }

        viewModel.apply {
            listQuizContent.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isListQuestionLoading = false

                        listQuestion.clear()
                        listQuestion.addAll(it.data)
                        listAnswer.clear()
                    }
                    is Result.Error -> {
                        isListQuestionLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getListQuestion()
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isListQuestionLoading = false
                    }
                }

                showLoading()
            }

            currentQuestion.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isQuestionLoading = false

                        setupQuestions(it.data)
                    }
                    is Result.Error -> {
                        isQuestionLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getQuestion(listQuestion[curQuestion])
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isQuestionLoading = false
                    }
                }

                showLoading()
            }

            sendAnswerResult.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        isSendAnswerLoading = false

                        setChildLevel(it.data)
                    }
                    is Result.Error -> {
                        isSendAnswerLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.sendAnswer(listAnswer)
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isSendAnswerLoading = false
                    }
                }
            }

            updateChildResult.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        AlertDialog.Builder(requireContext()).apply {
                            setMessage(R.string.have_took_test)
                            setPositiveButton(R.string.to_children_area) { _, _ ->
                                requireContext().preferences.setChosenChild(chosenChild?.id ?: error("must have chosen child"))
                                startActivity(Intent(requireContext(), ChildActivity::class.java))
                                activity?.finish()
                            }
                            setNegativeButton(R.string.back_to_dashboard) { _, _ ->
                                findNavController().navigate(R.id.action_placementQuizFragment_to_nav_dashboard)
                            }
                        }.create().show()

                    }
                    is Result.Error -> {
                        isUpdateChildLoading = false

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.sendAnswer(listAnswer)
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        isUpdateChildLoading = false
                    }
                }
            }
        }
    }

    private fun setChildLevel(_result: PostAnswerResponse) {
        // for now, child will always be level 1 regardless of result
        val child = chosenChild?.copy(level = 1) ?: error("must have chosen child")
        viewModel.updateChild(child)
    }

    private fun setupQuestions(multiQuestion: MultipleChoiceQuestion) {
        binding?.apply {
            title.text = getString(R.string.question_no_x, curQuestion + 1)

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

            choiceList.adapter = MultipleChoiceAdapter(ArrayList(multiQuestion.choices)).apply {
                setOnItemClickCallback(object : MultipleChoiceAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Choice, view: AnswerCard) {
                        currentAnswer = data.choiceName

                        showButton()
                    }
                })
            }

            root.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    private fun showLoading() {
        val loading =
            isListQuestionLoading || isQuestionLoading || isSendAnswerLoading || isUpdateChildLoading

        binding?.apply {
            placementQuizGroup.setVisible(!loading)
            placementQuizLoadingGroup.setVisible(loading)

            if (currentAnswer == null) {
                nextQuestionBtn.setVisible(false)
            }
        }
    }

    private fun showButton() {
        binding?.apply {
            nextQuestionBtn.setVisible(true)
            nextQuestionBtn.setText(
                if (curQuestion == listQuestion.size - 1) {
                    R.string.send_answers
                } else {
                    R.string.continue_label
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}