package com.hoanglong180903.driver.domain.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.domain.model.UserInfo

@Keep
data class GetShipperInfoResponse (
    val message : String? = null,
    val shipper : UserInfo?
)