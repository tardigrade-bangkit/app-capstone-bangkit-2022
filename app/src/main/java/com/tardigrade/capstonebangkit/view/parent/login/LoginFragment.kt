package com.tardigrade.capstonebangkit.view.parent.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.api.ApiConfig
import com.tardigrade.capstonebangkit.data.preference.SessionPreferences
import com.tardigrade.capstonebangkit.data.repository.AuthRepository
import com.tardigrade.capstonebangkit.databinding.FragmentLoginBinding
import com.tardigrade.capstonebangkit.misc.Result
import com.tardigrade.capstonebangkit.utils.*
import com.tardigrade.capstonebangkit.view.parent.register.RegisterFragment

internal val Context.preferences: SessionPreferences
    get() = SessionPreferences(this)

class LoginFragment : Fragment() {
    private val viewModel by viewModels<LoginViewModel> {
        LoginViewModel.Factory(
            AuthRepository(
                ApiConfig.getApiService(),
                requireContext().preferences
            )
        )
    }
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

        getActionBar(activity)?.hide()
        showLoading(false)

        val preferences = requireContext().preferences
        if(!preferences.getToken().isNullOrEmpty()) {
            loginSuccess(preferences.hasPin())
        }

        setFragmentResultListener(RegisterFragment.RESULT_KEY) { _, bundle ->
            val email = bundle.getString(RegisterFragment.RESULT_EMAIL)
            binding?.emailInputEt?.setText(email)
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

        viewModel.loggedIn.observe(viewLifecycleOwner) {
            when(it) {
                is Result.Success -> {
                    loginSuccess(it.data)
                    showLoading(false)
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

    private fun showLoading(loading: Boolean) {
        binding?.apply {
            loginLoadingGroup.setVisible(loading)
            loginGroup.setVisible(!loading)
        }
    }

    private fun tryLogin() {
        with(binding ?: return) {
            val emailValid = emailInput
                .validate(context, getString(R.string.email_input_label)) {
                    if (!it.isValidEmail()) {
                        return@validate getString(R.string.email_not_valid)
                    }

                    null
                }
            val passwordValid = passwordInput
                .validate(context, getString(R.string.password_input_label))

            if (!emailValid || !passwordValid) {
                return
            }

            val email = emailInputEt.text.toString()
            val password = passwordInputEt.text.toString()

            viewModel.login(email, password)
        }
    }

    private fun loginSuccess(hasPin: Boolean?) {
        if (hasPin == null) {
            return
        }

        val toPinFragment = LoginFragmentDirections
            .actionLoginFragmentToPinFragment().apply {
                this.hasPin = hasPin
            }

        findNavController().navigate(toPinFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}