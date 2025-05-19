package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.Order

data class GetDetailOrderResponse (
    val message : String? = null,
    val data : Order?
)