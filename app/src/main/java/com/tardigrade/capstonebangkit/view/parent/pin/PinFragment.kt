package com.tardigrade.capstonebangkit.view.parent.pin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.api.PinData
import com.tardigrade.capstonebangkit.data.repository.AuthRepository
import com.tardigrade.capstonebangkit.databinding.FragmentPinBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.getActionBar
import com.tardigrade.capstonebangkit.utils.hideSoftKeyboard
import com.tardigrade.capstonebangkit.utils.setVisible
import com.tardigrade.capstonebangkit.utils.showSnackbar
import com.tardigrade.capstonebangkit.view.parent.login.preferences

class PinFragment : Fragment() {
    private val viewModel by viewModels<PinViewModel> {
        PinViewModel.Factory(
            AuthRepository(
                ApiConfig.getApiService(),
                requireContext().preferences
            )
        )
    }
    private var binding: FragmentPinBinding? = null

    private var hasPin: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)

        getActionBar(activity)?.hide()
        hasPin = PinFragmentArgs.fromBundle(arguments as Bundle).hasPin

        binding?.apply {
            val pinInputs = arrayOf(
                pinInput1,
                pinInput2,
                pinInput3,
                pinInput4,
                pinInput5,
                pinInput6
            )

            pinInputs.forEach {
                it.addTextChangedListener(object : TextWatcher {
                    var numTemp: String? = null

                    override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                        numTemp = p0.toString()
                    }

                    override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                        // do nothing
                    }

                    override fun afterTextChanged(p0: Editable) {
                        for (i in pinInputs.indices) {
                            if (p0 === pinInputs[i].editableText) {
                                if (p0.isBlank()) {
                                    return
                                }

                                if (p0.toString().length > 1) {
                                    val newNum = p0.toString().substring(1, 2)
                                    if (newNum != numTemp) {
                                        pinInputs[i].setText(newNum)
                                    } else {
                                        pinInputs[i].setText(numTemp)
                                    }
                                }

                                if (i != pinInputs.lastIndex) {
                                    pinInputs[i + 1].requestFocus()
                                } else {
                                   hideSoftKeyboard(requireActivity())
                                }
                            }
                        }
                    }
                })
            }

            continueBtn.setOnClickListener {
                validatePin(pinInputs)
            }

            pinLabel.text = if (hasPin == true) {
                getString(R.string.pin_enter_label)
            } else {
                getString(R.string.pin_create_label)
            }
        }

        viewModel.pinResult.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    if (it.data == PinViewModel.CHECK_PIN) {
                        findNavController()
                            .navigate(PinFragmentDirections.actionPinFragmentToDashboardFragment())
                    } else {
                        findNavController()
                            .navigate(PinFragmentDirections.actionNavPinSelf().apply {
                                hasPin = true
                            })
                    }
                }
                is Result.Error -> {
                    showLoading(false)

                    val error = it.getErrorIfNotHandled()
                    if (!error.isNullOrEmpty()) {
                        binding?.root?.let { view ->
                            showSnackbar(view, error)
                        }
                    }
                }
                is Result.Loading -> showLoading(true)
            }
        }
    }

    private fun validatePin(pinInputs: Array<EditText>) {
        val pin = StringBuilder().apply {
            pinInputs.forEach {
                if (it.text.isBlank()) {
                    showSnackbar(binding?.root as View, getString(R.string.pin_not_valid))
                    return
                }

                this.append(it.text.toString())
            }
        }.toString()

        val token = requireContext().preferences.getToken() ?: ""

        if (hasPin == true) {
            viewModel.checkPin(token, PinData(pin))
        } else {
            viewModel.addPin(token, PinData(pin))
        }
    }

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            pinLoadingGroup.setVisible(loading)
            pinGroup.setVisible(!loading)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}