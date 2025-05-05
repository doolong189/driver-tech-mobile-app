package com.hoanglong180903.driver.data.enity

import androidx.annotation.Keep

@Keep
data class LoginAccountRequest (
    val email : String? = "",
    val password : String = ""
)