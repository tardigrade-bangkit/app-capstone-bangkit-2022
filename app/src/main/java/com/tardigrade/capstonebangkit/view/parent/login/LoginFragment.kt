package com.tardigrade.capstonebangkit.view.parent.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.databinding.FragmentLoginBinding

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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}