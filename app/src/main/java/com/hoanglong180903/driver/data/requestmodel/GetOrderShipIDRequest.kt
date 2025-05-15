package com.hoanglong180903.driver.data.requestmodel

data class GetOrderShipIDRequest (
    val idShipper : String? = "",
    val receiptStatus : Int? = 0
)