package com.hoanglong180903.driver.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.common.base.BaseAdapter
import com.hoanglong180903.driver.common.base.BaseViewHolder
import com.hoanglong180903.driver.databinding.ItemDeliveryBinding
import com.hoanglong180903.driver.model.Order
import com.hoanglong180903.driver.utils.Utils
import com.mapbox.geojson.Point

private var onClickDirectionMap: ((id: Order, position: Int) -> Unit)? = null
private var onClickCall: ((id: Order, position: Int) -> Unit)? = null
private var onClickDetailOrder: ((id: Order, position: Int) -> Unit)? = null
class HomeAdapter : BaseAdapter<HomeAdapter.HomeViewHolder>()
{
    private var orders : List<Order> = listOf()

    fun directionMap(id: ((id: Order, position: Int) -> Unit)) {
        onClickDirectionMap = id
    }
    fun call(id: ((id: Order, position: Int) -> Unit)) {
        onClickCall = id
    }
    fun detailOrder(id: ((id: Order, position: Int) -> Unit)) {
        onClickDetailOrder = id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemDeliveryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    class HomeViewHolder(private val binding: ItemDeliveryBinding) :
        BaseViewHolder<Order>(binding) {
        override fun bindView(item: Order) {
            super.bindView(item)
            binding.run {
                orderId.text = itemView.context.getString(R.string.order_id) + "1"
                time.text = Utils.convertTimestampToDate(item.date)
                totalOrders.text = Utils.formatPrice(item.feeDelivery) + "đ"

                icDirection.setOnClickListener {
                    onClickDirectionMap?.let {
                        it(item, adapterPosition)
                    }
                }
                icDetailOrder.setOnClickListener {
                    onClickDetailOrder?.let{
                        it(item, adapterPosition)
                    }
                }
                val origin = Point.fromLngLat(item.fromLocation!![1], item.fromLocation[0])
                val destination = Point.fromLngLat(item.toLocation!![1], item.toLocation[0])
                distance.text = "${item.distance} km"
                time.text = item.timer
            }
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindView(orders[position])
    }

    fun submitList(list : List<Order>){
        this.orders = list
        notifyDataSetChanged()
    }

}