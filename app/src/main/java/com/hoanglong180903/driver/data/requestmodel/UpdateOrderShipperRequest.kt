package com.hoanglong180903.driver.data.requestmodel

import androidx.annotation.Keep

@Keep
data class UpdateOrderShipperRequest (
    val orderId : String? = "",
    val idShipper : String ? = "",
)