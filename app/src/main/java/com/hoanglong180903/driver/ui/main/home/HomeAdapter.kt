package com.hoanglong180903.driver.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.hoanglong180903.driver.R
import com.hoanglong180903.driver.databinding.ItemDeliveryBinding
import com.hoanglong180903.driver.model.Order
import com.hoanglong180903.driver.utils.Utils
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.common.LifecycleService
import com.mapbox.geojson.Point
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var onClickDirectionMap: ((id: Order, position: Int) -> Unit)? = null
private var onClickCall: ((id: Order, position: Int) -> Unit)? = null
private var onClickDetailOrder: ((id: Order, position: Int) -> Unit)? = null
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val binding =
            ItemDeliveryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    class HomeViewHolder(private val binding: ItemDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Order) {
            binding.run {
                orderId.text = itemView.context.getString(R.string.order_id) + "1"
                time.text = Utils.convertTimestampToDate(item.date)
                totalOrders.text = Utils.formatPrice(item.totalPrice) + "đ"

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