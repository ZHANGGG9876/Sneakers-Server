package com.sneakers_shop_server.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sneakers_shop_server.databinding.SneakerItemListBinding
import com.sneakers_shop_server.model.SneakerModel

class SneakerCardHomeAdapter(private val clickListener: SneakerHomeListener) :
    ListAdapter<SneakerModel, SneakerCardHomeAdapter.SneakerViewHolder>(
        DiffCallback
    ) {

    override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
        return SneakerViewHolder(
            SneakerItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    class SneakerViewHolder(
        var binding: SneakerItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SneakerHomeListener, item: SneakerModel) {
            binding.sneaker = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<SneakerModel>() {
        override fun areItemsTheSame(oldItem: SneakerModel, newItem: SneakerModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SneakerModel, newItem: SneakerModel): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
class SneakerHomeListener(val clickListener: (sneaker: SneakerModel) -> Unit) {
    fun onClick(sneaker: SneakerModel) = clickListener(sneaker)
}