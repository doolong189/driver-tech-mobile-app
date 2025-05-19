package com.hoanglong180903.driver.data.usecase

import com.hoanglong180903.driver.data.requestmodel.GetUserInfoRequest
import com.hoanglong180903.driver.data.requestmodel.LoginAccountRequest
import com.hoanglong180903.driver.data.requestmodel.RegisterAccountRequest
import com.hoanglong180903.driver.data.remote.RetrofitInstance

class UserRepository {

    suspend fun registerAccount(request : RegisterAccountRequest) = RetrofitInstance.api.registerAccount(request)

    suspend fun loginAccount(request : LoginAccountRequest) = RetrofitInstance.api.loginAccount(request)

    suspend fun getUserInfo(request : GetUserInfoRequest) = RetrofitInstance.api.getUserInfo(request)
}