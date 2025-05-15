package com.hoanglong180903.driver.data.requestmodel

import androidx.annotation.Keep

@Keep
data class GetNewOrderRequest (
    val receiptStatus : Int = 0
)