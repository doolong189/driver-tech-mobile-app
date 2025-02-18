package com.hoanglong180903.driver.presentation.ui.main

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
import com.hoanglong180903.driver.domain.response.BillOrderResponse

class OrderAdapter(private val context : Context)  : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private var list: List<BillOrderResponse> = listOf()

    class OrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvUserName : TextView = itemView.findViewById(R.id.item_order_tvUserName)
        val tvAddress : TextView = itemView.findViewById(R.id.item_order_tvUserAddress)
        val tvTime : TextView = itemView.findViewById(R.id.item_order_tvTime)
        val tvListDetaiProduct : TextView = itemView.findViewById(R.id.item_order_tvListProductDetail)
        val imageUser : ImageView = itemView.findViewById(R.id.item_order_imgUser)
        val btnPaymentMethod : Button = itemView.findViewById(R.id.item_order_tvPaymentMethod)
        val btnCancel : Button = itemView.findViewById(R.id.item_order_btnCancel)
        val btnAccept : Button = itemView.findViewById(R.id.item_order_btnAccept)
        fun onBind(item : BillOrderResponse){
            val clientTemp = item.client
            if (clientTemp.name != "") {
                tvUserName.text = item.client.name
            }else{
                tvUserName.text = "---"
            }
            tvAddress.text = item.client.address
            tvTime.text = "Thời gian: " +item.date
            tvListDetaiProduct.text = "${item.products.size} mặt hàng"
            Glide.with(itemView.context).load(item.store.image).into(imageUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_oder,parent,false)
        return OrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    fun setOrderList(orders: BillOrderResponse) {
        list = listOf(orders)
        notifyDataSetChanged()
    }
}