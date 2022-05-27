package com.tardigrade.capstonebangkit.view.children.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentHomeBinding
import com.tardigrade.capstonebangkit.misc.isValidEmail
import com.tardigrade.capstonebangkit.misc.validate
import com.tardigrade.capstonebangkit.view.parent.register.RegisterFragment

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>()
    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}