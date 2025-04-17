package com.hoanglong180903.driver.ui.dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hoanglong180903.driver.databinding.ItemDeliveryBinding
import com.hoanglong180903.driver.domain.model.Order

private var onClickDirectionMap: ((id: Order, position: Int) -> Unit)? = null
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var orders : List<Order> = listOf()

    fun directionMap(id: ((id: Order, position: Int) -> Unit)) {
        onClickDirectionMap = id
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val binding = ItemDeliveryBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return HomeViewHolder(binding)
    }

    class HomeViewHolder(private val binding: ItemDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Order) {
            binding.run {
                orderId.text = item._id
                time.text = item.date
                Glide.with(binding.root.context)
                    .load(item.products[0].product.idUser.image)
                    .into(circleImageView)
                cardView.setOnClickListener {
                    onClickDirectionMap?.let {
                        it(item, adapterPosition)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(orders[position])
    }

    fun submitList(list : List<Order>){
        this.orders = list
        notifyDataSetChanged()
    }

}