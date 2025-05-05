package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep
import com.hoanglong180903.driver.model.UserInfo

@Keep
data class LoginAccountResponse (
    var message : String? = null,
    var shipper : UserInfo?
)