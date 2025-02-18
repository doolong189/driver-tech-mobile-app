package com.hoanglong180903.driver.data.repository

import com.hoanglong180903.driver.data.remote.RetrofitInstance

class OrderRepository {
    suspend fun getOrder() = RetrofitInstance.api.requestGetBillOrder()
}