package com.tardigrade.capstonebangkit.view.parent.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentDashboardBinding
import com.tardigrade.capstonebangkit.databinding.FragmentProfileBinding
import com.tardigrade.capstonebangkit.view.parent.dashboard.DashboardViewModel

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel>()
    private var binding: FragmentProfileBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}