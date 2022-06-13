package com.tardigrade.capstonebangkit.view.parent.childcreated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentChildCreatedBinding
import com.tardigrade.capstonebangkit.utils.getActionBar

class ChildCreatedFragment : Fragment() {
    private val viewModel by viewModels<ChildCreatedViewModel>()
    private var binding: FragmentChildCreatedBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildCreatedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createdChild = ChildCreatedFragmentArgs.fromBundle(arguments as Bundle).createdChild

        getActionBar(activity)?.apply {
            show()
            setTitle(R.string.creation_success_title)
        }

        binding?.apply {
            takeTestBtn.setOnClickListener {
                findNavController()
                    .navigate(
                        ChildCreatedFragmentDirections
                            .actionChildCreatedFragmentToPlacementQuizFragment(createdChild)
                    )
            }

            dashboardBtn.setOnClickListener {
                findNavController()
                    .navigate(
                        ChildCreatedFragmentDirections
                            .actionChildCreatedFragmentToNavDashboard()
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}