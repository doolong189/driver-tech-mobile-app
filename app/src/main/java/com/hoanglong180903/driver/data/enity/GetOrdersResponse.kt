package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.Order
import com.hoanglong180903.driver.model.Product

@Keep
data class GetOrdersResponse(
   val message : String? = null,
   val data : List<Order>?
)