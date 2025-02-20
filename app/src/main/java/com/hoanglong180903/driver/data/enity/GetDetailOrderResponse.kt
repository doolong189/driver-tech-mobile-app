package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.Order

@Keep
class GetDetailOrderResponse (
    val message : String? = null,
    val data : Order?
)