package com.hoanglong180903.driver.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class Product(
    val _id: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val discount : Int,
    val description: String,
    val image: List<String>,
    val idUser: UserInfo,
    val idCategory: Category
)