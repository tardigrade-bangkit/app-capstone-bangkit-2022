package com.tardigrade.capstonebangkit.view.parent.pin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentPinBinding

class PinFragment : Fragment() {
    private val viewModel by viewModels<PinViewModel>()
    private var binding: FragmentPinBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            val pinInputs = arrayOf(
                pinInput1,
                pinInput2,
                pinInput3,
                pinInput4,
                pinInput5,
                pinInput6
            )

            var textWatcher = object : TextWatcher {
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
                                pinInputs[i + 1].setSelection(pinInputs[i + 1].length())
                            } else {
                                checkPin(pinInputs)
                            }
                        }
                    }
                }
            }

            pinInput1.addTextChangedListener(textWatcher)
            pinInput2.addTextChangedListener(textWatcher)
            pinInput3.addTextChangedListener(textWatcher)
            pinInput4.addTextChangedListener(textWatcher)
            pinInput5.addTextChangedListener(textWatcher)
            pinInput6.addTextChangedListener(textWatcher)
        }
    }

    private fun checkPin(pinInputs: Array<EditText>) {
        val pin = StringBuilder().apply {
            pinInputs.forEach {
                this.append(it.text.toString())
            }
        }.toString()

        Toast.makeText(context, "Pin: $pin", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_pinFragment_to_dashboardFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}