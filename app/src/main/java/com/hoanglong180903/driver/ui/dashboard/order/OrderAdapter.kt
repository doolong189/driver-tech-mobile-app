package com.hoanglong180903.driver.ui.dashboard.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ItemOderBinding
import com.hoanglong180903.driver.model.Order
import com.hoanglong180903.driver.utils.Utils

private var onClickShowDetail: ((id: Order, position: Int) -> Unit)? = null
private var onClickItemOrderAccept : ((id : Order , position : Int) -> Unit)? = null
private var onClickItemOrderCancel : ((id : Order , position : Int) -> Unit)? = null
class OrderAdapter  : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private var list: List<Order> = listOf()

    fun showDetailNotification(id: ((id: Order, position: Int) -> Unit)) {
        onClickShowDetail = id
    }

    fun onClickItemOrderAccept(id: ((id: Order, position: Int) -> Unit)) {
        onClickItemOrderAccept = id
    }

    fun onClickItemOrderCancel(id: ((id: Order, position: Int) -> Unit)) {
        onClickItemOrderCancel= id
    }

    class OrderViewHolder(private val binding: ItemOderBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(item: Order) {
            binding.run {
                itemOrderTvTime.text = "${Utils.getTimeDifference(item.date)}"
                itemOrderTvUserName.text = "${binding.root.context.getString(R.string.store)}: ${item.products[0].product.idUser.name}"
                itemOrderTvUserAddress.text = "${binding.root.context.getString(R.string.address)}: ${item.products[0].product.idUser.address}"
                itemOrderTvListProductDetail.text = "${item.products.size} ${binding.root.context.getString(R.string.total_product)}"
                Glide.with(binding.root.context)
                    .load(item.products[0].product.idUser.image)
                    .into(itemOrderImgUser)
                itemView.setOnClickListener {
                    onClickShowDetail?.let {
                        it(item, adapterPosition)
                    }
                }

                itemOrderBtnAccept.setOnClickListener {
                    onClickItemOrderAccept?.let {
                        it(item, adapterPosition)
                    }
                }

                itemOrderBtnCancel.setOnClickListener {
                    onClickItemOrderCancel?.let {
                        it(item,adapterPosition)
                    }
                }
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