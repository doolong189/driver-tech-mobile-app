package com.hoanglong180903.driver.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class Product(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("idUser") val user: User,
    @SerializedName("__v") val version: Int,
    @SerializedName("idCategory") val category: Category
)