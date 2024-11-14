package com.example.goldpad.ui.personalRequests

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goldpad.R
import com.example.goldpad.database.entities.Request
import com.example.goldpad.databinding.FragmentPersonalRequestsBinding
import com.example.goldpad.ui.adapters.UserRequestsAdapter
import com.example.goldpad.utils.Constants
import com.example.goldpad.viewmodels.PersonalRequestsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonalRequestsFragment : Fragment() {

    private var _binding: FragmentPersonalRequestsBinding? = null
    private val binding get() = _binding!!
    private val personalRequestsViewModel: PersonalRequestsViewModel by viewModels()
    @Inject
    lateinit var userRequestsAdapter: UserRequestsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalRequestsBinding.inflate(layoutInflater, container, false)
        personalRequestsViewModel.fetchRequests()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            personalRequestsViewModel.requestsLiveData.observe(viewLifecycleOwner) { requests ->
                if(requests.isEmpty())
                {
                    emptyListText.visibility = View.VISIBLE
                    recyclerViewRequests.visibility = View.GONE
                }
                else
                {
                    emptyListText.visibility = View.GONE
                    recyclerViewRequests.visibility = View.VISIBLE
                }


                userRequestsAdapter.setData(requests)
                recyclerViewRequests.layoutManager = LinearLayoutManager(context)
                recyclerViewRequests.adapter = userRequestsAdapter

            }

            newRequestButton.setOnClickListener {
                showNewRequestDialog()
            }

            userRequestsAdapter.onDeleteClick = { request ->
                showDeleteConfirmationDialog(request)
            }

            userRequestsAdapter.onEditClick = { request ->
                showNewRequestDialog(request)
            }
        }
    }

    private fun showNewRequestDialog(request: Request? = null) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_new_request, null)
        val modeSpinner = dialogView.findViewById<Spinner>(R.id.modeSpinner)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)

        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.request_types, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeSpinner.adapter = adapter


        request?.let {
            modeSpinner.setSelection(if (it.mode) 0 else 1)
            amountEditText.setText(it.amount.toString())
        }

        val dialogTitle = if (request == null) "ایجاد درخواست جدید" else "ویرایش درخواست"
        val positiveButtonText = if (request == null) "ایجاد" else "ویرایش"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle(dialogTitle)
            .setNegativeButton("بیخیال", null)
            .setPositiveButton(positiveButtonText, null)
            .create()

        dialog.show()


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val amountText = amountEditText.text.toString()
            val amount = amountText.toIntOrNull() ?: -1

            if (amountText.isEmpty() || amount <= 0) {
                Toast.makeText(requireContext(), "لطفا مقدار معتبر برای طلا وارد کنید", Toast.LENGTH_SHORT).show()
            } else {
                val mode = modeSpinner.selectedItem.toString() == "خرید"

                if (request == null) {
                    val newRequest = Request(
                        fromUser = Constants.USER_ID,
                        requestText = "",
                        amount = amount,
                        mode = mode
                    )
                    personalRequestsViewModel.insertRequest(newRequest)
                } else {
                    val updatedRequest = request.copy(
                        amount = amount,
                        mode = mode
                    )
                    personalRequestsViewModel.updateRequest(updatedRequest)
                }

                dialog.dismiss()
            }
        }
    }


    private fun showDeleteConfirmationDialog(request: Request) {
        AlertDialog.Builder(requireContext())
            .setTitle("حذف درخواست")
            .setMessage("آیا از حذف این درخواست مطمئن هستید؟")
            .setNegativeButton("بیخیال", null)
            .setPositiveButton("حذف") { _, _ ->
                personalRequestsViewModel.deleteRequest(request)
                Toast.makeText(requireContext(), "حذف این درخواست با موفقیت انجام شد", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}