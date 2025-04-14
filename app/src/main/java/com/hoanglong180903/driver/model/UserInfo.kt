package com.hoanglong180903.driver.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UserInfo(
    val name: String,
    val address: String,
    val password: String,
    val email: String,
    val phone: String,
    val image: String,
    val location: List<Double>,
    val token : String,
)