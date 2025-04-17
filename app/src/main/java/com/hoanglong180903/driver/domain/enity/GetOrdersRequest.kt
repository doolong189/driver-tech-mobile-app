package com.hoanglong180903.driver.domain.enity

import androidx.annotation.Keep

@Keep
data class GetOrdersRequest (
    val receiptStatus : Int = 0
)