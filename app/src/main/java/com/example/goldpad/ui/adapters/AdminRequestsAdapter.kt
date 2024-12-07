package com.example.goldpad.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldpad.database.entities.Request
import com.example.goldpad.databinding.AdminRequestItemBinding

class AdminRequestsAdapter : RecyclerView.Adapter<AdminRequestsAdapter.ViewHolder>() {

    private var requestList = listOf<Request>()
    private val proposedValues = mutableMapOf<Request, String>()

    var onProposedValueChanged: ((Request, String) -> Unit)? = null

    inner class ViewHolder(private val binding: AdminRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(request: Request) {
            binding.apply {
                sellerName.text = if (request.mode) {
                    "خریدار: ${request.userId}"
                } else {
                    "فروشنده: ${request.userId}"
                }

                proposedValueInput.setText(proposedValues[request] ?: request.proposedValue?.toString() ?: "")

                proposedValueInput.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        val value = proposedValueInput.text.toString()
                        proposedValues[request] = value
                        onProposedValueChanged?.invoke(request, value)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdminRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int = requestList.size

    fun setRequests(requests: List<Request>) {
        requestList = requests
        notifyDataSetChanged()
    }

    fun getProposedValues(): Map<Request, String> {
        return proposedValues
    }
}
