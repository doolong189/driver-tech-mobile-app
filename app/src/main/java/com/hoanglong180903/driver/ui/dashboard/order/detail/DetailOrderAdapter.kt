package com.hoanglong180903.driver.ui.dashboard.order.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ItemDetailProductBinding
import com.hoanglong180903.driver.domain.model.ProductOfOrder
import com.hoanglong180903.driver.utils.Utils

class DetailOrderAdapter : RecyclerView.Adapter<DetailOrderAdapter.DetailOrderViewHolder>() {
    private var list: List<ProductOfOrder> = listOf()
    class DetailOrderViewHolder(
        private val binding: ItemDetailProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: ProductOfOrder) {
            binding.run {
                Glide.with(binding.root.context)
                    .load(item.product.image)
                    .into(imageProduct)
                tvProduct.text = "${binding.root.context.getString(R.string.product)}: ${item.product.name}"
                tvDescription.text = "${item.product.description}"
                tvAmount.text = "${binding.root.context.getString(R.string.amount)}: ${item.quantity}"
                tvPrice.text = "${binding.root.context.getString(R.string.price)}: ${Utils.formatPrice(item.product.price!!)} đ"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailOrderViewHolder {
        val binding =
            ItemDetailProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailOrderViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DetailOrderViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    fun submitList(productOfOrder: List<ProductOfOrder>) {
        list = productOfOrder
        notifyDataSetChanged()
    }
}