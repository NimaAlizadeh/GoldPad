package com.example.goldpad.ui.allSellerRequests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldpad.databinding.FragmentAllSellerRequestsBinding
import com.example.goldpad.ui.adapters.AllSellerRequestsAdapter
import com.example.goldpad.viewmodels.AllSellerRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            viewModel.saveSelectedRequestsToWaiting(userId = 1) // Replace with actual userId
        }

    }

    private fun setupRecyclerView() {
        adapter = AllSellerRequestsAdapter().apply {
            onRequestSelected = { requestWithUser ->
                viewModel.toggleRequestSelection(requestWithUser)
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AllSellerRequestsFragment.adapter
        }
    }


    private fun observeViewModel() {
        viewModel.requestsWithUsersLiveData.observe(viewLifecycleOwner) { requestsWithUsers ->
            adapter.setRequests(requestsWithUsers)
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