package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.UserInfo

data class GetUserInfoResponse (
    val message : String? = null,
    val users : UserInfo?
)