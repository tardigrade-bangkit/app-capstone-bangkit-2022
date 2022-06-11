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
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.model.User
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.databinding.FragmentProfileBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.loadImage
import com.tardigrade.capstonebangkit.utils.setVisible
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.parent.login.preferences

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModel.Factory(
            ProfileRepository(ApiConfig.getApiService()),
            requireContext().preferences.getToken() ?: throw IllegalStateException("must have token")
        )
    }
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

        showProfileLoading(false)
        showChildrenLoading(false)

        viewModel.apply {
            myProfile.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Success -> {
                        showProfileLoading(false)
                        setMyProfile(it.data)
                    }
                    is Result.Error -> {
                        showProfileLoading(false)

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getMyProfile()
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        showProfileLoading(true)
                    }
                }
            }

            children.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Success -> {
                        showChildrenLoading(false)

                        setChildrenData(it.data)
                    }
                    is Result.Error -> {
                        showChildrenLoading(false)

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getChildren()
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        showChildrenLoading(true)
                    }
                }
            }
        }

        binding?.apply {
            logoutButton.setOnClickListener {
                logout()
            }

            editInfoAccountButton.setOnClickListener {
                showSnackbar(binding?.root as View, getString(R.string.not_available_yet))
            }
        }
    }

    private fun logout() {
        requireContext().preferences.resetSession()
        findNavController().navigate(R.id.action_nav_profile_to_nav_login)
    }

    private fun setMyProfile(user: User) {
        binding?.apply {
            profileAvatar.loadImage(user.avatar)
            profileName.text = user.name
            profileEmail.text = user.email
        }
    }

    private fun setChildrenData(children: List<ChildProfile>) {
        binding?.profileChilds?.adapter = ChildProfileSmallAdapter(ArrayList(children)).apply {
            setOnItemClickCallback(object : ChildProfileSmallAdapter.OnItemClickCallback {
                override fun onItemClicked(child: ChildProfile) {
                    val toChildProfile = ProfileFragmentDirections
                        .actionNavProfileToChildProfileFragment().apply {
                            this.child = child
                        }

                    findNavController().navigate(toChildProfile)
                }

                override fun onFooterClicked() {
                    findNavController().navigate(R.id.action_nav_profile_to_childProfileFragment)
                }
            })
        }
    }

    private fun showChildrenLoading(loading: Boolean) {
        binding?.apply {
            childrenLoading.setVisible(loading)
            profileChilds.setVisible(!loading)
        }
    }

    private fun showProfileLoading(loading: Boolean) {
        binding?.apply {
            profileLoading.setVisible(loading)
            profileLoadingGroup.setVisible(!loading)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}