package com.tardigrade.capstonebangkit.view.child.quiz

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.model.MaterialContent
import com.tardigrade.capstonebangkit.databinding.FragmentMaterialBinding
import com.tardigrade.capstonebangkit.view.child.LessonContentViewModel

class MultipleChoiceQuizFragment : Fragment() {
    private val viewModel by viewModels<QuizViewModel>()
    private val lessonContentViewModel by viewModels<LessonContentViewModel>()
    private var binding: FragmentMaterialBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMaterialBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnBack.setOnClickListener {findNavController().navigate(R.id.action_materialFragment_to_homeFragment)}
            btnNextSlide.setOnClickListener {nextSlide()}
            btnPreviousSlide.setOnClickListener {previousSlide()}
        }
    }

    private fun setMaterialContent(content: MaterialContent) {
        TODO("not yet implemented")
    }

    private fun nextSlide() {
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