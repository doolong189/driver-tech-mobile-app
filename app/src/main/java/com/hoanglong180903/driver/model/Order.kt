package com.hoanglong180903.driver.model


data class Order(
    val _id: String,
    val totalPrice: Double,
    val date : String,
    val receiptStatus : Int,
    val idClient : UserInfo,
    val idShipper : UserInfo,
    val products : List<ProductOfOrder>,
    val fromLocation : List<Double>?,
    val toLocation : List<Double>?,
    val distance : Float,
    val timer : String,
    val feeDelivery : Double
)
