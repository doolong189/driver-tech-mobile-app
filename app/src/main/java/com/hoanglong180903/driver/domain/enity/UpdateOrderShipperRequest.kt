package com.hoanglong180903.driver.domain.enity

import androidx.annotation.Keep

@Keep
data class UpdateOrderShipperRequest (
    val orderId : String? = "",
    val idShipper : String ? = "",
)