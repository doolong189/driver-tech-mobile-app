package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.UserInfo

data class LoginAccountResponse (
    var message : String? = null,
    var user : UserInfo?
)