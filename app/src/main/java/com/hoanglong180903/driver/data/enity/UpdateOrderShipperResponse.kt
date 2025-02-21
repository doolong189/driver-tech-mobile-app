package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.Order

@Keep
data class UpdateOrderShipperResponse (
    val message : String? = null,
    val updatedOrder : Order?
)