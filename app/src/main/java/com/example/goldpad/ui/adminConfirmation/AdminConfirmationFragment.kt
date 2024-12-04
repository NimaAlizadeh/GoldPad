package com.example.goldpad.ui.adminConfirmation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldpad.database.dto.RequestWithUser
import com.example.goldpad.databinding.FragmentAdminConfirmationBinding
import com.example.goldpad.ui.adapters.AdminRequestsAdapter
import com.example.goldpad.viewmodels.AdminConfirmationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminConfirmationFragment : Fragment() {

    private var _binding: FragmentAdminConfirmationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AdminConfirmationViewModel by viewModels()
    private lateinit var adapter: AdminRequestsAdapter
    private var waitingId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            waitingId = it.getInt("waitingId", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.confirmButton.setOnClickListener {
            showConfirmationDialog()
        }

        viewModel.fetchWaitingWithRequests(waitingId)
    }

    private fun setupRecyclerView() {
        adapter = AdminRequestsAdapter().apply {
            onProposedValueChanged = { requestWithUser, value ->
                viewModel.updateProposedValue(requestWithUser.request, value)
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AdminConfirmationFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.waitingWithRequestsLiveData.observe(viewLifecycleOwner) { waitingWithRequests ->
            adapter.setRequests(waitingWithRequests.requests.map {
                RequestWithUser(it, waitingWithRequests.user)
            })
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("تایید نهایی")
            .setMessage("آیا از تایید اطلاعات وارد شده مطمئن هستید؟")
            .setPositiveButton("ادامه") { _, _ ->
                viewModel.confirmRequests(waitingId)
            }
            .setNegativeButton("انصراف", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
