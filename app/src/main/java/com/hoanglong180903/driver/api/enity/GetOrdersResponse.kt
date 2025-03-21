package com.hoanglong180903.driver.api.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.Order

@Keep
data class GetOrdersResponse(
   val message : String? = null,
   val data : List<Order>?
)