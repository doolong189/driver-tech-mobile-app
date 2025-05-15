package com.hoanglong180903.driver.data.responsemodel

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.UserInfo

@Keep
data class GetShipperInfoResponse (
    val message : String? = null,
    val shipper : UserInfo?
)