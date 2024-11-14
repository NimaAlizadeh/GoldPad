package com.example.goldpad.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.goldpad.database.entities.Request
import com.example.goldpad.databinding.UserRequestAdapterItemBinding
import javax.inject.Inject

class UserRequestsAdapter @Inject constructor() : RecyclerView.Adapter<UserRequestsAdapter.CustomViewHolder>(){

    lateinit var binding: UserRequestAdapterItemBinding
    private var requestList = emptyList<Request>()

    var onDeleteClick: ((Request) -> Unit)? = null
    var onEditClick: ((Request) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder
    {
        binding = UserRequestAdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int)
    {
        holder.setData(requestList[position])
//        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = requestList.size

    inner class CustomViewHolder: RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun setData(tempItem: Request)
        {
            binding.apply {
                amountDynamicText.text = tempItem.amount.toString() + " گرم "
                if(tempItem.mode)
                    // being true means user is going to buy
                    requestTypeDynamicText.text = "خرید"
                else
                    requestTypeDynamicText.text = "فروش"


                deleteButton.setOnClickListener {
                    onDeleteClick?.invoke(tempItem)
                }

                editButton.setOnClickListener {
                    onEditClick?.invoke(tempItem)
                }
            }
        }
    }

    fun setData(newListData: List<Request>)
    {
        val notesDiffUtils = UserRequestDiffUtils(requestList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        requestList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class UserRequestDiffUtils(private val oldItem: List<Request>, private val newItem: List<Request>) : DiffUtil.Callback(){
        override fun getOldListSize() = oldItem.size

        override fun getNewListSize() = newItem.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition].id == newItem[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition].id == newItem[newItemPosition].id && oldItem[oldItemPosition].amount == newItem[newItemPosition].amount
        }
    }
}