package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.Order

data class GetNewOrderResponse(
   val message : String? = null,
   val data : List<Order>?
)