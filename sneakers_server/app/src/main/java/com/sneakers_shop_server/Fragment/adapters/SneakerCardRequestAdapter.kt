package com.sneakers_shop_server.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sneakers_shop_server.databinding.SneakerItemRequestBinding
import com.sneakers_shop_server.model.OrderModel

class OrderCardRequestAdapter(private val clickListener: OrderRequestListener) :
    ListAdapter<OrderModel, OrderCardRequestAdapter.OrderViewHolder>(
        DiffCallback
    ) {

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            SneakerItemRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class OrderViewHolder(
        var binding: SneakerItemRequestBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OrderRequestListener, item: OrderModel) {
            binding.order = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<OrderModel>() {
        override fun areItemsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: OrderModel, newItem: OrderModel): Boolean {
            return oldItem.userName == newItem.userName
        }
    }
}

class OrderRequestListener(val clickListener: (order: OrderModel) -> Unit) {
    fun onClick(order: OrderModel) = clickListener(order)
}