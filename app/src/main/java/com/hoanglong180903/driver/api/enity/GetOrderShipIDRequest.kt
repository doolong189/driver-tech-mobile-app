package com.hoanglong180903.driver.api.enity

data class GetOrderShipIDRequest (
    val idShipper : String? = "",
    val receiptStatus : Int? = 0
)