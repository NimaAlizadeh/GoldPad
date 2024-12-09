package com.example.goldpad.ui.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.goldpad.database.entities.Request
import com.example.goldpad.databinding.AdminRequestItemBinding

class AdminRequestsAdapter : RecyclerView.Adapter<AdminRequestsAdapter.ViewHolder>() {

    private var requestList = listOf<Request>()
    private val proposedValues = mutableMapOf<Request, String>()
    private var currentTextWatcher: TextWatcher? = null


    var onProposedValueChanged: ((Request, String) -> Unit)? = null

    inner class ViewHolder(private val binding: AdminRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(request: Request) {
            binding.apply {
                sellerOrBuyer.text = if (request.mode) {
                    "خریدار " + request.amount + " گرم طلا "
                } else {
                    "فروشنده " + request.amount + " گرم طلا "
                }

                // مقدار اولیه را تنظیم کنید
                proposedValueInput.setText(proposedValues[request] ?: request.proposedValue?.toString() ?: "")

                // اگر TextWatcher فعلی وجود دارد، آن را حذف کنید
                currentTextWatcher?.let { proposedValueInput.removeTextChangedListener(it) }

                // ایجاد TextWatcher جدید
                val textWatcher = object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val value = s.toString()
                        proposedValues[request] = value
                        onProposedValueChanged?.invoke(request, value)
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                }

                // اضافه کردن TextWatcher جدید
                proposedValueInput.addTextChangedListener(textWatcher)

                // ذخیره TextWatcher جدید برای حذف در آینده
                currentTextWatcher = textWatcher
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
