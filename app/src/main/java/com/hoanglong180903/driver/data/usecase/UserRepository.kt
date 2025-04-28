package com.hoanglong180903.driver.data.usecase

import com.hoanglong180903.driver.data.enity.GetShipperInfoRequest
import com.hoanglong180903.driver.data.enity.RegisterAccountRequest
import com.hoanglong180903.driver.data.remote.RetrofitInstance

class UserRepository {

    suspend fun registerAccount(request : RegisterAccountRequest) = RetrofitInstance.api.registerAccount(request)
    suspend fun getShipperInfo(request : GetShipperInfoRequest) = RetrofitInstance.api.getShipperInfo(request)
}