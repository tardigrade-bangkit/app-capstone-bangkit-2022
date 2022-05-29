package com.tardigrade.capstonebangkit.view.parent.childprofile

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.AvatarAdapter
import com.tardigrade.capstonebangkit.databinding.FragmentChildProfileBinding
import com.tardigrade.capstonebangkit.misc.getActionBar
import com.tardigrade.capstonebangkit.misc.loadImage
import com.tardigrade.capstonebangkit.misc.validate
import java.text.SimpleDateFormat
import java.util.*

class ChildProfileFragment : Fragment() {
    private val viewModel by viewModels<ChildProfileViewModel>()
    private var binding: FragmentChildProfileBinding? = null

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode = ChildProfileFragmentArgs.fromBundle(arguments as Bundle).mode
        val tookTest = false

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

        binding?.apply {
            listAvatar.adapter = AvatarAdapter(
                arrayListOf(
                    "https://i.pravatar.cc/300?u=1",
                    "https://i.pravatar.cc/300?u=2",
                    "https://i.pravatar.cc/300?u=3",
                    "https://i.pravatar.cc/300?u=4",
                    "https://i.pravatar.cc/300?u=5",
                    "https://i.pravatar.cc/300?u=6",
                    "https://i.pravatar.cc/300?u=7",
                    "https://i.pravatar.cc/300?u=8",
                    "https://i.pravatar.cc/300?u=9",
                    "https://i.pravatar.cc/300?u=10",
                    "https://i.pravatar.cc/300?u=11",
                    "https://i.pravatar.cc/300?u=12",
                )
            ).apply {
                setOnItemClickCallback(object : AvatarAdapter.OnItemClickCallback {
                    override fun onItemClicked(imageUrl: String) {
                        childAvatar.loadImage(imageUrl)
                    }
                })
            }

            childAvatar.loadImage("https://i.pravatar.cc/300?u=99")

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

        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val ADD_MODE = 0
        const val EDIT_MODE = 1
    }
}