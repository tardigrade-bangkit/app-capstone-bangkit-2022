package com.tardigrade.capstonebangkit.view.parent.choosechild

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.adapter.ChildProfileBigAdapter
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.repository.ProfileRepository
import com.tardigrade.capstonebangkit.databinding.FragmentChooseChildBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.setVisible
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.parent.login.preferences

class ChooseChildFragment : Fragment() {
    private val viewModel by viewModels<ChooseChildViewModel> {
        ChooseChildViewModel.Factory(
            ProfileRepository(ApiConfig.getApiService()),
            requireContext().preferences.getToken()
                ?: throw IllegalStateException("must have token")
        )
    }
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
        showChildrenLoading(false)

        viewModel.children.observe(viewLifecycleOwner) {
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

    private fun showChildrenLoading(loading: Boolean) {
        binding?.apply {
            childrenLoading.setVisible(loading)
            listChildren.setVisible(!loading)
        }
    }

    private fun setChildrenData(children: List<ChildProfile>) {
        binding?.listChildren?.adapter = ChildProfileBigAdapter(ArrayList(children))
            .apply {
                setOnItemClickCallback(object : ChildProfileBigAdapter.OnItemClickCallback {
                    override fun onItemClicked(child: ChildProfile) {
                        if (child.level == 0) {
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage(R.string.no_test_warning)
                                setPositiveButton(R.string.take_test) { _, _ ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}