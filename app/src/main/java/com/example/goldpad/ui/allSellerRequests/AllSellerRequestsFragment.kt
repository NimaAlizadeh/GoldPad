package com.example.goldpad.ui.allSellerRequests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.goldpad.R
import com.example.goldpad.databinding.FragmentAllSellerRequestsBinding
import com.example.goldpad.ui.adapters.AllSellerRequestsAdapter
import com.example.goldpad.utils.Constants
import com.example.goldpad.viewmodels.AllSellerRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllSellerRequestsFragment : Fragment() {

    private var _binding: FragmentAllSellerRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllSellerRequestsViewModel by viewModels()
    private lateinit var adapter: AllSellerRequestsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSellerRequestsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.continueButton.setOnClickListener {
            val selectedRequests = adapter.getSelectedRequests()
            if (selectedRequests.isNotEmpty()) {
                lifecycleScope.launch {
                    val waitingId = viewModel.saveSelectedRequestsToWaiting(Constants.USER_ID)
                    Toast.makeText(requireContext(), waitingId.toString(), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        AllSellerRequestsFragmentDirections
                            .actionAllSellerRequestsFragmentToAllBuyerRequestsFragment(waitingId)
                    )
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = AllSellerRequestsAdapter()
        adapter.onRequestSelected = { requestWithUser ->
            viewModel.toggleRequestSelection(requestWithUser)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllSellerRequestsFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.requestsLiveData.observe(viewLifecycleOwner) { requestsWithUsers ->
            val activeRequests = requestsWithUsers.filter { it.request.isActive }
            adapter.setRequests(activeRequests)
        }

        viewModel.selectedRequests.observe(viewLifecycleOwner) { selectedRequests ->
            adapter.setSelectedRequests(selectedRequests.toSet())
            binding.continueButton.isEnabled = selectedRequests.isNotEmpty()
        }

        viewModel.fetchRequests()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
