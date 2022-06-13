package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.model.ArrangeWordsQuestion
import com.tardigrade.capstonebangkit.databinding.FragmentArrangeWordsQuizBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.showSnackbar

class ArrangeWordsQuizFragment : Fragment() {
    private val viewModel by viewModels<ArrangeWordsQuizViewModel>()
    private var binding: FragmentArrangeWordsQuizBinding? = null
    private var quizFragment: QuizFragment = this.parentFragment as QuizFragment

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
                                viewModel.getArrangeWordsQuestion(quizFragment.viewModel.currentQuizContent.value?.arrangeWordsId!!)
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
            Toast.makeText(context, sentence, Toast.LENGTH_SHORT).show()
        }

        viewModel.getArrangeWordsQuestion(quizFragment.viewModel.currentQuizContent.value?.arrangeWordsId!!)

        binding?.apply {
            btnNext.setOnClickListener { quizFragment.nextQuestion() }
            btnPrevious.setOnClickListener { quizFragment.previousQuestion() }
            btnBack.setOnClickListener { quizFragment.backToHome() }
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
            with(quizFragment.lessonContentViewModel) {
                btnContentList.text = StringBuilder()
                    .append(currentLesson?.title).append(" - ")
                    .append(currentLessonContent?.title).append(" - ")
                    .append(content.order)
            }
        }
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