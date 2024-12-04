package com.example.goldpad.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldpad.database.dto.RequestWithUser
import com.example.goldpad.databinding.AdminRequestItemBinding

class AdminRequestsAdapter : RecyclerView.Adapter<AdminRequestsAdapter.ViewHolder>() {

    private var requestList = listOf<RequestWithUser>() // لیست درخواست‌ها
    private val proposedValues = mutableMapOf<RequestWithUser, String>() // مقادیر پیشنهادی

    var onProposedValueChanged: ((RequestWithUser, String) -> Unit)? = null // کال‌بک برای تغییر مقدار پیشنهادی

    inner class ViewHolder(private val binding: AdminRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(requestWithUser: RequestWithUser) {
            binding.apply {
                val request = requestWithUser.request
                val user = requestWithUser.user

                // Display user role based on the mode attribute
                sellerName.text = if (request.mode) {
                    "خریدار: ${user.username ?: "ناشناس"}"
                } else {
                    "فروشنده: ${user.username ?: "ناشناس"}"
                }

                // Set proposed value
                proposedValueInput.setText(proposedValues[requestWithUser] ?: request.proposedValue?.toString() ?: "")

                // Handle proposed value changes
                proposedValueInput.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) { // When the input loses focus
                        val newValue = proposedValueInput.text.toString()
                        proposedValues[requestWithUser] = newValue
                        onProposedValueChanged?.invoke(requestWithUser, newValue)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdminRequestItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int = requestList.size

    // بروزرسانی لیست درخواست‌ها
    fun setRequests(newRequests: List<RequestWithUser>) {
        requestList = newRequests
        notifyDataSetChanged()
    }

    // دریافت مقدار پیشنهادی جدید برای درخواست‌ها
    fun getProposedValues(): Map<RequestWithUser, String> {
        return proposedValues
    }
}
