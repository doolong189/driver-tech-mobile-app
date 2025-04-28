package com.hoanglong180903.driver.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserInfo(
    val name: String? = null,
    val address: String? = null ,
    val password: String? = null ,
    val email: String? = null ,
    val phone: String? = null ,
    val image: String? = null ,
    val location: List<Double>?,
    val token : String? = null ,
)