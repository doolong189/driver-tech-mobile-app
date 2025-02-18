package com.hoanglong180903.driver.domain.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.hoanglong180903.driver.domain.model.Product
import com.hoanglong180903.driver.domain.model.User

data class BillOrderResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("totalAmount") val totalAmount: String,
    @SerializedName("date") val date: String,
    @SerializedName("receiptStatus") val receiptStatus: String,
    @SerializedName("idClient") val client: User,
    @SerializedName("idStore") val store: User,
    @SerializedName("idShipper") val shipperId: String,
    @SerializedName("products") val products: List<Product>
)