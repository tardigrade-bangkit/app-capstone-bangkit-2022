package com.tardigrade.capstonebangkit.view.parent.profile

import android.os.Bundle
import android.util.Log
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
import com.tardigrade.capstonebangkit.data.repository.ChildrenDataRepository
import com.tardigrade.capstonebangkit.databinding.FragmentProfileBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.loadImage
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.parent.childprofile.ChildProfileFragment
import com.tardigrade.capstonebangkit.view.parent.login.preferences

class ProfileFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModel.Factory(
            ChildrenDataRepository(ApiConfig.getApiService()),
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

        viewModel.apply {
            children.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Success -> {
                        setChildrenData(it.data)
                    }
                    is Result.Error -> {
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

                    }
                }
            }
        }

        binding?.apply {
            // placeholder
            profileAvatar.setImageResource(R.drawable.ic_launcher_background)
            profileName.text = "Nama"
            profileEmail.text = "Email@email.com"

            logoutButton.setOnClickListener {
                logout()
            }
        }
    }

    private fun logout() {
        requireContext().preferences.resetSession()
        findNavController().navigate(R.id.action_nav_profile_to_nav_login)
    }

    private fun setChildrenData(children: List<ChildProfile>) {
        binding?.profileChilds?.adapter = ChildProfileSmallAdapter(ArrayList(children)).apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}