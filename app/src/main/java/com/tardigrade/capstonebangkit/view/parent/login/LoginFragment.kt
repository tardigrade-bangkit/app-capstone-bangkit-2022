package com.tardigrade.capstonebangkit.view.parent.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentLoginBinding
import com.tardigrade.capstonebangkit.misc.isValidEmail
import com.tardigrade.capstonebangkit.misc.validate
import com.tardigrade.capstonebangkit.view.parent.register.RegisterFragment

class LoginFragment : Fragment() {
    private val viewModel by viewModels<LoginViewModel>()
    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(RegisterFragment.RESULT_KEY) { _, bundle ->
            val email = bundle.getString(RegisterFragment.RESULT_EMAIL)
            binding?.emailInput?.setText(email)
        }

        binding?.apply {
            registerBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment,
                    null,
                    null,
                    FragmentNavigatorExtras(
                        loginTitle to loginTitle.transitionName,
                        emailInputLabel to emailInputLabel.transitionName,
                        emailInput to emailInput.transitionName,
                        passwordInputLabel to passwordInputLabel.transitionName,
                        passwordInput to passwordInput.transitionName,
                        loginBtn to loginBtn.transitionName,
                        registerLabel to registerLabel.transitionName,
                        registerBtn to registerBtn.transitionName
                    )
                )
            }

            loginBtn.setOnClickListener {
                tryLogin()
            }
        }
    }

    private fun tryLogin() {
        with(binding ?: return) {
            val emailValid = emailInput
                .validate(context, getString(R.string.email_input_label)) {
                    if (!it.text.toString().isValidEmail()) {
                        it.error = getString(R.string.email_not_valid)
                        return@validate false
                    }

                    true
                }
            val passwordValid = passwordInput
                .validate(context, getString(R.string.password_input_label))

            if (!emailValid || !passwordValid) {
                return
            }

            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            loginSuccess()
        }
    }

    private fun loginSuccess() {
        findNavController().navigate(R.id.action_loginFragment_to_pinFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}