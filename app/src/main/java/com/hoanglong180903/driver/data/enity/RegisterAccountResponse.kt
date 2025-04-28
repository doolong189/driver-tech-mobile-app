package com.hoanglong180903.driver.data.enity

import com.hoanglong180903.driver.model.UserInfo

data class RegisterAccountResponse (
    val message : String?,
    val user : UserInfo?
)