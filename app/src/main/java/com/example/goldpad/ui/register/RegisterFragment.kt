package com.example.goldpad.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.goldpad.R
import com.example.goldpad.databinding.FragmentRegisterBinding
import com.example.goldpad.viewmodels.RegisterState
import com.example.goldpad.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            registerButton.setOnClickListener {
                val username = username.text.toString().trim()
                val password = password.text.toString().trim()
                val firstName = firstname.text.toString().trim()
                val lastName = lastname.text.toString().trim()
                val bankName = bankName.text.toString().trim()
                val userId = userId.text.toString().trim()
                val shebaCode = shebaCode.text.toString().trim()


                if (username.isEmpty() || password.isEmpty() || firstName.isEmpty()
                    || lastName.isEmpty() || bankName.isEmpty() || userId.isEmpty()
                    || shebaCode.isEmpty()) {
                    Toast.makeText(requireContext(), "لطفا تمام قسمت ها را تکمیل کنید", Toast.LENGTH_SHORT).show()
                } else {
                    registerViewModel.register(username, password, firstName, lastName, bankName, userId, shebaCode)
                }
            }


            registerViewModel.registerState.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is RegisterState.Loading -> {
                        binding.registerButton.isEnabled = false
                        registerButton.visibility = View.GONE
                        registerProgress.visibility = View.VISIBLE
                    }
                    is RegisterState.Success -> {
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToPersonalRequestsFragment())
                    }
                    is RegisterState.Error -> {
                        binding.registerButton.isEnabled = true
                        registerButton.visibility = View.VISIBLE
                        registerProgress.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        binding.registerButton.isEnabled = true
                        registerButton.visibility = View.VISIBLE
                        registerProgress.visibility = View.GONE
                    }
                }
            })


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}