package com.tardigrade.capstonebangkit.view.child.quiz

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tardigrade.capstonebangkit.R

class ArrangeWordQuizFragment : Fragment() {

    companion object {
        fun newInstance() = ArrangeWordQuizFragment()
    }

    private lateinit var viewModel: ArrangeWordQuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_arrange_word_quiz, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ArrangeWordQuizViewModel::class.java)
        // TODO: Use the ViewModel
    }

}