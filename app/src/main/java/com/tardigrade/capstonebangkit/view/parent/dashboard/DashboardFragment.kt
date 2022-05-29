package com.tardigrade.capstonebangkit.view.parent.dashboard

import androidx.lifecycle.ViewModelProvider
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
import com.tardigrade.capstonebangkit.databinding.FragmentDashboardBinding
import com.tardigrade.capstonebangkit.databinding.FragmentLoginBinding
import com.tardigrade.capstonebangkit.misc.getActionBar
import com.tardigrade.capstonebangkit.misc.isValidEmail
import com.tardigrade.capstonebangkit.misc.validate
import com.tardigrade.capstonebangkit.view.parent.login.LoginViewModel
import com.tardigrade.capstonebangkit.view.parent.register.RegisterFragment

class DashboardFragment : Fragment() {
    private val viewModel by viewModels<DashboardViewModel>()
    private var binding: FragmentDashboardBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getActionBar(activity)?.apply {
            show()
            setTitle(R.string.stat_title)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}