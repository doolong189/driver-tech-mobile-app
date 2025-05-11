package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.Order

@Keep
data class GetNewOrderResponse(
   val message : String? = null,
   val data : List<Order>?
)