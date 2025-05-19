package com.hoanglong180903.driver.data.responsemodel


data class GetStatisticalResponse (
    val message : String? = null,
    val totalOrdersCount : Int? = null,
    val completedOrdersCount : Int? = null,
    val canceledOrdersCount : Int? = null,
    val totalReceivedAmount : Double? = null
)