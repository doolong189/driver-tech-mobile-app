package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep

@Keep
data class UpdateOrderShipperRequest (
    val orderId : String? = "",
    val idShipper : String ? = "",
)