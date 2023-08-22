package com.sneakers_shop_server.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sneakers_shop_server.databinding.SneakerItemOrderBinding
import com.sneakers_shop_server.model.OrderModel

class OrderCardOrderAdapter(private val clickListener: OrderListener) :
    ListAdapter<OrderModel, OrderCardOrderAdapter.OrderViewHolder>(
        DiffCallback
    ) {

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            SneakerItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    class OrderViewHolder(
        var binding: SneakerItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: OrderListener, item: OrderModel) {
            binding.orderShipped = item
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

class OrderListener(val clickListener: (order_shipped: OrderModel) -> Unit) {
    fun onClick(order_shipped: OrderModel) = clickListener(order_shipped)
}