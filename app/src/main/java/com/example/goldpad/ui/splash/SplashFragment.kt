package com.example.goldpad.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.goldpad.databinding.FragmentSplashBinding
import com.example.goldpad.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE

        splashViewModel.checkAndInsertAdminUser()
        splashViewModel.checkUserToken()

        splashViewModel.navigateToAdminPage.observe(viewLifecycleOwner, Observer { shouldNavigateToAdmin ->
            binding.progressBar.visibility = View.GONE
            if (shouldNavigateToAdmin == true) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAllSellerRequestsFragment())
            }
        })

        splashViewModel.navigateToPersonalRequest.observe(viewLifecycleOwner, Observer { shouldNavigateToPersonalRequest ->
            binding.progressBar.visibility = View.GONE
            if (shouldNavigateToPersonalRequest) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToPersonalRequestsFragment())
            }
        })

        splashViewModel.navigateToLogin.observe(viewLifecycleOwner, Observer { shouldNavigateToLogin ->
            binding.progressBar.visibility = View.GONE
            if (shouldNavigateToLogin) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }
        })
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