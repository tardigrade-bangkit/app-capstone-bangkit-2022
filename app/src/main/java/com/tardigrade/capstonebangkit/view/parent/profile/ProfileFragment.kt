package com.tardigrade.capstonebangkit.view.parent.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.ChildProfileSmallAdapter
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.databinding.FragmentProfileBinding
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.view.parent.childprofile.ChildProfileFragment

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

        getActionBar(activity)?.apply {
            show()
            setTitle(R.string.profile_title)
        }

        binding?.apply {
            // placeholder
            profileAvatar.setImageResource(R.drawable.ic_launcher_background)
            profileName.text = "Nama"
            profileEmail.text = "Email@email.com"

            profileChilds.adapter = ChildProfileSmallAdapter(arrayListOf(
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
                ChildProfile(
                    avatarUrl = "https://i.pravatar.cc/300",
                    name = "test"
                ),
            )).apply {
                setOnItemClickCallback(object : ChildProfileSmallAdapter.OnItemClickCallback {
                    override fun onItemClicked(child: ChildProfile) {
                        val toChildProfile = ProfileFragmentDirections
                            .actionNavProfileToChildProfileFragment().apply {
                                mode = ChildProfileFragment.EDIT_MODE
                            }

                        findNavController().navigate(toChildProfile)
                    }

                    override fun onFooterClicked() {
                        findNavController().navigate(R.id.action_nav_profile_to_childProfileFragment)
                    }

                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}