package com.tardigrade.capstonebangkit.view.parent.childprofile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.AvatarAdapter
import com.tardigrade.capstonebangkit.data.api.AddChild
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.Avatar
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.databinding.FragmentChildProfileBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.*
import com.tardigrade.capstonebangkit.view.parent.login.preferences
import java.text.SimpleDateFormat
import java.util.*

class ChildProfileFragment : Fragment() {
    private val viewModel by viewModels<ChildProfileViewModel> {
        ChildProfileViewModel.Factory(
            ProfileRepository(ApiConfig.getApiService()),
            requireContext().preferences.getToken()
                ?: error("must have token")
        )
    }
    private var binding: FragmentChildProfileBinding? = null

    private val calendar = Calendar.getInstance()

    private var child: ChildProfile? = null

    private var choosenAvatar: Avatar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        child = ChildProfileFragmentArgs.fromBundle(arguments as Bundle).child

        getActionBar(activity)?.apply {
            show()
            setTitle(
                if (child == null) {
                    R.string.child_profile_add_title
                } else {
                    R.string.child_profile_edit_title
                }
            )
        }

        showAvatarLoading(false)
        showAddChildLoading(false)

        viewModel.apply {
            avatars.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        showAvatarLoading(false)

                        choosenAvatar = if (child == null) {
                            binding?.childAvatar?.loadImage(it.data[0].url)
                            it.data[0]
                        } else {
                            it.data.find { avatar ->
                                avatar.url == child?.avatarUrl
                            }
                        }

                        setAvatarsData(it.data)
                    }
                    is Result.Error -> {
                        showAvatarLoading(false)

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getAvatars()
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        showAvatarLoading(true)
                    }
                }
            }

            addChildrenResult.observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        showAddChildLoading(false)

                        findNavController()
                            .navigate(
                                ChildProfileFragmentDirections
                                    .actionChildProfileFragmentToChildCreatedFragment(it.data)
                            )
                    }
                    is Result.Error -> {
                        showAddChildLoading(false)

                        val error = it.getErrorIfNotHandled()
                        if (!error.isNullOrEmpty()) {
                            binding?.root?.let { view ->
                                showSnackbar(view, error, getString(R.string.try_again)) {
                                    viewModel.getAvatars()
                                }
                            }
                        }
                    }
                    is Result.Loading -> {
                        showAddChildLoading(true)
                    }
                }
            }
        }

        binding?.apply {
            saveBtn.text = getString(
                if (child == null) {
                    R.string.add_child_profile
                } else {
                    R.string.save_child_profile
                }
            )
            saveBtn.setVisible(child == null)
            saveBtn.setOnClickListener {
                saveChild()
            }

            noTestWarning.visibility = if (child?.level == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }

            if (child != null) {
                val choosenChild = child

                childAvatar.loadImage(choosenChild?.avatarUrl ?: "")
                nameInputEt.setText(choosenChild?.name)
                nameInputEt.isClickable = false
                nameInputEt.isFocusable = false
                birthdateInputEt.setText(choosenChild?.birthdate)
                birthdateInputEt.isClickable = false
                birthdateInputEt.isFocusable = false

                testBtn.setOnClickListener {
                    val navigateTest =
                        ChildProfileFragmentDirections.actionChildProfileFragmentToPlacementQuizFragment(
                            choosenChild ?: return@setOnClickListener
                        )

                    findNavController().navigate(navigateTest)
                }
            } else {
                birthdateInputEt.setOnClickListener {
                    DatePickerDialog(
                        requireContext(),
                        { _, year, month, day ->
                            calendar.set(year, month, day)
                            birthdateInputEt
                                .setText(
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                        .format(calendar.time)
                                )
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                    ).show()
                }
            }
        }
    }

    private fun showAvatarLoading(loading: Boolean) {
        binding?.apply {
            avatarLoading.setVisible(loading)
            avatarLoadingGroup.setVisible(!loading)

            if (child != null) {
                listAvatar.visibility = View.GONE
            }
        }
    }

    private fun showAddChildLoading(loading: Boolean) {
        binding?.apply {
            addChildLoading.setVisible(loading)
            addChildLoadingGroup.setVisible(!loading)

            if (child != null) {
                listAvatar.visibility = View.GONE
            }
        }
    }

    private fun saveChild() {
        with(binding ?: return) {
            val nameValid = nameInput.validate(context, getString(R.string.name_input_label))
            val birthdateValid =
                birthdateInput.validate(context, getString(R.string.birthdate_input_label))

            if (!nameValid || !birthdateValid) {
                return
            }

            if (child == null) {
                viewModel.addChildren(
                    AddChild(
                        nameInput.editText?.text.toString(),
                        birthdateInput.editText?.text.toString(),
                        choosenAvatar?.id ?: 0
                    )
                )
            } else {
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setAvatarsData(avatars: List<Avatar>) {
        binding?.listAvatar?.adapter = AvatarAdapter(ArrayList(avatars))
            .apply {
                setOnItemClickCallback(object : AvatarAdapter.OnItemClickCallback {
                    override fun onItemClicked(avatar: Avatar) {
                        binding?.childAvatar?.loadImage(avatar.url)
                        choosenAvatar = avatar
                    }
                })
            }
    }
}