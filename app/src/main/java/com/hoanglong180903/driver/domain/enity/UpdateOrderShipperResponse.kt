package com.hoanglong180903.driver.domain.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.domain.model.Order

@Keep
data class UpdateOrderShipperResponse (
    val message : String? = null,
    val updatedOrder : Order?
)