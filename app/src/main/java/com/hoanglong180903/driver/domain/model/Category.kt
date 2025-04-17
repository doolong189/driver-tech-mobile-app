package com.hoanglong180903.driver.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Category(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("__v") val version: Int
) {
    override fun toString(): String {
        return name
    }
}