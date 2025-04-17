package com.hoanglong180903.driver.domain.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.domain.model.Order

@Keep
data class GetOrdersResponse(
   val message : String? = null,
   val data : List<Order>?
)