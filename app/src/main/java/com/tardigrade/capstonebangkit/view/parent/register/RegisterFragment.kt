package com.tardigrade.capstonebangkit.view.parent.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentLoginBinding
import com.tardigrade.capstonebangkit.databinding.FragmentRegisterBinding
import com.tardigrade.capstonebangkit.misc.isValidEmail
import com.tardigrade.capstonebangkit.misc.validate
import com.tardigrade.capstonebangkit.view.parent.login.LoginViewModel

class RegisterFragment : Fragment() {
    private val viewModel by viewModels<RegisterViewModel>()
    private var binding: FragmentRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            loginBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            registerBtn.setOnClickListener {
                tryRegister()
            }
        }
    }

    private fun tryRegister() {
        with(binding ?: return) {
            val emailValid = emailInput
                .validate(context, getString(R.string.email_input_label)) {
                    if (!it.isValidEmail()) {
                        return@validate getString(R.string.email_not_valid)
                    }

                    null
                }
            val nameValid = nameInput
                .validate(context, getString(R.string.name_input_label))
            val passwordValid = passwordInput
                .validate(context, getString(R.string.password_input_label)) {
                    if(it.length < 8) {
                        return@validate getString(R.string.password_min_length)
                    }

                    null
                }
            val confirmPasswordValid = confirmPasswordInput
                .validate(context, getString(R.string.confirm_password_input_label))

            if (!emailValid || !nameValid || !passwordValid || !confirmPasswordValid) {
                return
            }

            val email = emailInputEt.text.toString()
            val name = nameInputEt.text.toString()
            val password = passwordInputEt.text.toString()
            val confirmPassword = confirmPasswordInputEt.text.toString()

            registerSuccess(email)
        }
    }

    private fun registerSuccess(email: String) {
        setFragmentResult(RESULT_KEY, bundleOf(
            RESULT_EMAIL to email
        ))

        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val RESULT_KEY = "register"
        const val RESULT_EMAIL = "result_email"
    }
}