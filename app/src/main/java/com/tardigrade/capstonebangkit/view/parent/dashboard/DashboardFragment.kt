package com.tardigrade.capstonebangkit.view.parent.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentDashboardBinding
import com.tardigrade.capstonebangkit.utils.getActionBar

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