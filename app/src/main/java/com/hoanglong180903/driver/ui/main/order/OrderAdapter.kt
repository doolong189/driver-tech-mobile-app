package com.hoanglong180903.driver.ui.main.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.data.enity.GetOrdersResponse
import com.hoanglong180903.driver.databinding.ItemOderBinding
import com.hoanglong180903.driver.model.Order

class OrderAdapter  : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private var list: List<Order> = listOf()

    class OrderViewHolder(private val binding: ItemOderBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(item: Order) {
            binding.run {
                itemOrderTvTime.text = item.date
                itemOrderTvUserName.text = item.products[0].product.idUser.name
                itemOrderTvUserAddress.text = item.products[0].product.idUser.address
                itemOrderTvListProductDetail.text = ""+item.products.size
                Glide.with(binding.root.context)
                    .load(item.products[0].product.idUser.image)
                    .into(itemOrderImgUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    fun setOrderList(orders: List<Order>) {
        list = orders
        notifyDataSetChanged()
    }
}