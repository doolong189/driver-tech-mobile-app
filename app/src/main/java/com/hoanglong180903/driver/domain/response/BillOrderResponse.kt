package com.hoanglong180903.driver.domain.response

import com.hoanglong180903.driver.domain.model.Product
import com.hoanglong180903.driver.domain.model.User

data class BillOrderResponse (
    val id : String,
    val totalAmount : String,
    val date : String,
    val receiptStatus : String,
    val idClient : User,
    val idStore : User,
    val idShipper : String,
    val products : List<Product>
)