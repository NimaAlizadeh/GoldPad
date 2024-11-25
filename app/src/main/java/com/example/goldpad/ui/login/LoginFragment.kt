package com.example.goldpad.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.goldpad.R
import com.example.goldpad.databinding.FragmentLoginBinding
import com.example.goldpad.viewmodels.LoginState
import com.example.goldpad.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "لطفا تمام قسمت ها را تکمیل نمایید", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(username, password)
            }
        }

        loginViewModel.loginState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is LoginState.Loading -> {
                    binding.apply {
                        loginButton.isEnabled = false
                        loginButton.visibility = View.GONE
                        loginProgress.visibility = View.VISIBLE
                    }
                }
                is LoginState.Error -> {
                    binding.apply {
                        loginButton.isEnabled = true
                        loginButton.visibility = View.VISIBLE
                        loginProgress.visibility = View.GONE
                    }
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is LoginState.Success -> {
                    if (state.isAdmin) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToAllSellerRequestsFragment())
                    } else {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPersonalRequestsFragment())
                    }
                }

                else -> {
                    binding.apply {
                        loginButton.isEnabled = true
                        loginButton.visibility = View.VISIBLE
                        loginProgress.visibility = View.GONE
                    }
                }
            }
        })


        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}