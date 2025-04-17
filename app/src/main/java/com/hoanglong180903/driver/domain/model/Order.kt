package com.hoanglong180903.driver.domain.model


data class Order(
    val _id: String,
    val totalPrice: Double,
    val date : String,
    val receiptStatus : Int,
    val idClient : UserInfo,
    val idShipper : UserInfo,
    val products : List<ProductOfOrder>
)
