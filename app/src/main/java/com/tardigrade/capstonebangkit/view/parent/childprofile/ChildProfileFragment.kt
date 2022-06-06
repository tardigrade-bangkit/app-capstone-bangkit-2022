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
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.Avatar
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.databinding.FragmentChildProfileBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.loadImage
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.utils.validate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChildProfileFragment : Fragment() {
    private val viewModel by viewModels<ChildProfileViewModel> {
        ChildProfileViewModel.Factory(
            ProfileRepository(ApiConfig.getApiService())
        )
    }
    private var binding: FragmentChildProfileBinding? = null

    private val calendar = Calendar.getInstance()

    private var mode: Int? = null
    private var tookTest = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mode = ChildProfileFragmentArgs.fromBundle(arguments as Bundle).mode

        getActionBar(activity)?.apply {
            show()
            setTitle(
                if (mode == ADD_MODE) {
                    R.string.child_profile_add_title
                } else {
                    R.string.child_profile_edit_title
                }
            )
        }

        viewModel.apply {
            avatars.observe(viewLifecycleOwner) {
                when(it) {
                    is Result.Success -> {
                        binding?.childAvatar?.loadImage(it.data[0].url)

                        setAvatarsData(it.data)
                    }
                    is Result.Error -> {
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

                    }
                }
            }
        }

        binding?.apply {
            birthdateInputEt.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, day ->
                        calendar.set(year, month, day)
                        birthdateInputEt
                            .setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(calendar.time))
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                ).show()
            }

            saveBtn.text = getString(
                if (mode == ADD_MODE) {
                    R.string.add_child_profile
                } else {
                    R.string.save_child_profile
                }
            )
            saveBtn.setOnClickListener {
                saveChild()
            }

            noTestWarning.visibility = if (tookTest) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun saveChild() {
        val nameValid = binding?.nameInput?.validate(context, getString(R.string.name_input_label))
        val birthdateValid = binding?.birthdateInput?.validate(context, getString(R.string.birthdate_input_label))

        if (nameValid != true || birthdateValid != true) {
            return
        }

        if (mode == ADD_MODE) {
            findNavController()
                .navigate(ChildProfileFragmentDirections
                    .actionChildProfileFragmentToChildCreatedFragment())
        } else {
            findNavController().navigateUp()
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
                    }
                })
            }
    }

    companion object {
        const val ADD_MODE = 0
        const val EDIT_MODE = 1
    }
}