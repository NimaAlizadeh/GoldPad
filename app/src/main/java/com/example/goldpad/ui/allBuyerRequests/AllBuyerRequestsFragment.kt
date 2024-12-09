package com.example.goldpad.ui.allBuyerRequests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldpad.databinding.FragmentAllBuyerRequestsBinding
import com.example.goldpad.ui.adapters.AllSellerRequestsAdapter
import com.example.goldpad.viewmodels.AllBuyerRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllBuyerRequestsFragment : Fragment() {

    private var _binding: FragmentAllBuyerRequestsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllBuyerRequestsViewModel by viewModels()
    private lateinit var adapter: AllSellerRequestsAdapter
    private var waitingId: Int = 0 // Holds the ID of the Waiting record

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            waitingId = it.getInt("waitingId", 0) // Retrieve the waiting ID passed from the previous fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllBuyerRequestsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.continueButton.setOnClickListener {
            val selectedRequests = adapter.getSelectedRequests()
            if (selectedRequests.isNotEmpty()) {
                viewModel.saveSelectedRequestsToWaiting(waitingId, selectedRequests)
                viewModel.saveOperationCompleted.observe(viewLifecycleOwner) { isCompleted ->
                    if (isCompleted) {
                        val action = AllBuyerRequestsFragmentDirections
                            .actionAllBuyerRequestsFragmentToAdminConfirmationFragment(waitingId)
                        findNavController().navigate(action)
                    }
                }
            }
        }


        binding.cancelButton.setOnClickListener {
            Toast.makeText(requireContext(), waitingId.toString(), Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                viewModel.deleteWaitingRecord(waitingId)
                findNavController().navigate(
                    AllBuyerRequestsFragmentDirections
                        .actionAllBuyerRequestsFragmentToAllSellerRequestsFragment()
                )
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
            adapter = this@AllBuyerRequestsFragment.adapter
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


        viewModel.fetchBuyerRequests()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
