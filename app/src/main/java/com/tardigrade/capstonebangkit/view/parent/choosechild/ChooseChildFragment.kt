package com.tardigrade.capstonebangkit.view.parent.choosechild

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.ChildProfileBigAdapter
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.databinding.FragmentChooseChildBinding
import com.tardigrade.capstonebangkit.utils.getActionBar

class ChooseChildFragment : Fragment() {
    private val viewModel by viewModels<ChooseChildViewModel>()
    private var binding: FragmentChooseChildBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseChildBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getActionBar(activity)?.hide()

        binding?.apply {
            listChildren.adapter = ChildProfileBigAdapter(
                arrayListOf(
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        level = 1,
                        tookTest = true
                    ),
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        level = 2,
                        tookTest = true
                    ),
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        level = 1,
                        tookTest = true
                    ),
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        level = 2,
                        tookTest = true
                    ),
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        level = 1,
                        tookTest = true
                    ),
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        level = 2,
                        tookTest = true
                    ),
                    ChildProfile(
                        avatarUrl = "https://i.pravatar.cc/300",
                        name = "test",
                        tookTest = false
                    ),
                )
            ).apply {
                setOnItemClickCallback(object : ChildProfileBigAdapter.OnItemClickCallback {
                    override fun onItemClicked(child: ChildProfile) {
                        if (!child.tookTest) {
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage(R.string.no_test_warning)
                                setPositiveButton(R.string.take_test) { dialogInterface, _ ->
                                    Toast.makeText(
                                        context,
                                        "Test Choosen: $child",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                setNegativeButton(R.string.cancel) { dialogInterface, _ ->
                                    dialogInterface.cancel()
                                }
                            }.create().show()
                        } else {
                            Toast.makeText(context, "Choosen: $child", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFooterClicked() {
                        findNavController().navigate(R.id.action_chooseChildFragment_to_childProfileFragment)
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