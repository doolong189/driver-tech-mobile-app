package com.hoanglong180903.driver.domain.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.domain.model.Order

@Keep
class GetDetailOrderResponse (
    val message : String? = null,
    val data : Order?
)