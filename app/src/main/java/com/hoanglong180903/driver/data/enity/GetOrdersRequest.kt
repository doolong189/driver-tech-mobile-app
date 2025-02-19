package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep

@Keep
data class GetOrdersRequest (
    val receiptStatus : Int = 0
)