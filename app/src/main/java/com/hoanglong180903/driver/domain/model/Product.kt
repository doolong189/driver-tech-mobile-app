package com.hoanglong180903.driver.domain.model

import java.util.Locale


data class Product(
    val _id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val description: String = "",
    val image: String = "",
    val idUser: User? = null,
    val idCategory: Locale.Category? = null,
)