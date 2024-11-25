package com.example.goldpad.ui.adapters

import com.example.goldpad.database.dto.RequestWithUser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldpad.databinding.AllRequestAdapterItemBinding

class AllSellerRequestsAdapter : RecyclerView.Adapter<AllSellerRequestsAdapter.ViewHolder>() {

    private var requestList = listOf<RequestWithUser>()
    private var selectedRequests = mutableSetOf<RequestWithUser>()

    var onRequestSelected: ((RequestWithUser) -> Unit)? = null

    inner class ViewHolder(private val binding: AllRequestAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(requestWithUser: RequestWithUser) {
            binding.apply {
                val request = requestWithUser.request
                val user = requestWithUser.user

                amountDynamicText.text = "${request.amount} گرم"
                requestTypeDynamicText.text = if (request.mode) "خرید" else "فروش"
                username.text = user.username ?: "ناشناس"

                root.isSelected = selectedRequests.contains(requestWithUser)

                root.setOnClickListener {
                    onRequestSelected?.invoke(requestWithUser)
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

    fun setRequests(newRequests: List<RequestWithUser>) {
        requestList = newRequests
        notifyDataSetChanged()
    }

    fun setSelectedRequests(selected: Set<RequestWithUser>) {
        selectedRequests = selected.toMutableSet()
        notifyDataSetChanged()
    }
}
