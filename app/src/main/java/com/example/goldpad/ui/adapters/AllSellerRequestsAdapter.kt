package com.example.goldpad.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldpad.database.dto.RequestWithUser
import com.example.goldpad.databinding.AllRequestAdapterItemBinding

class AllSellerRequestsAdapter : RecyclerView.Adapter<AllSellerRequestsAdapter.ViewHolder>() {

    private var requestList = listOf<RequestWithUser>() // List of all requests
    private var selectedRequests = mutableSetOf<RequestWithUser>() // Track selected requests

    var onRequestSelected: ((RequestWithUser) -> Unit)? = null // Callback for selection

    inner class ViewHolder(private val binding: AllRequestAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(requestWithUser: RequestWithUser) {
            binding.apply {
                val request = requestWithUser.request
                val user = requestWithUser.user

                // Bind data to views
                amountDynamicText.text = "${request.amount} گرم"
                requestTypeDynamicText.text = if (request.mode) "خرید" else "فروش"
                username.text = user.username ?: "ناشناس"

                // Update selection indicator visibility
                selectionIndicator.visibility =
                    if (selectedRequests.contains(requestWithUser)) View.VISIBLE else View.GONE

                // Handle item click for selection and deselection
                root.setOnClickListener {
                    if (selectedRequests.contains(requestWithUser)) {
                        selectedRequests.remove(requestWithUser) // Deselect item
                    } else {
                        selectedRequests.add(requestWithUser) // Select item
                    }
                    notifyItemChanged(adapterPosition) // Update the specific item
                    onRequestSelected?.invoke(requestWithUser) // Trigger callback
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AllRequestAdapterItemBinding.inflate(
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

    // Update the list of requests
    fun setRequests(newRequests: List<RequestWithUser>) {
        requestList = newRequests
        notifyDataSetChanged()
    }

    // Set the selected requests from outside the adapter
    fun setSelectedRequests(selected: Set<RequestWithUser>) {
        selectedRequests = selected.toMutableSet()
        notifyDataSetChanged()
    }

    // Get the currently selected requests
    fun getSelectedRequests(): List<RequestWithUser> {
        return selectedRequests.toList()
    }
}
