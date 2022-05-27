package com.tardigrade.capstonebangkit.view.parent.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.ChildProfileAdapter
import com.tardigrade.capstonebangkit.data.model.ChildProfile
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // placeholder
            profileAvatar.setImageResource(R.drawable.ic_launcher_background)
            profileName.text = "Nama"
            profileEmail.text = "Email@email.com"

            profileChilds.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            profileChilds.adapter = ChildProfileAdapter(arrayListOf(
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://picsum.photos/200",
                    name = "test"
                ),
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}