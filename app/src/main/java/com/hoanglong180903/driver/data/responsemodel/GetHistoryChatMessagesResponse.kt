package com.hoanglong180903.driver.data.responsemodel

import com.hoanglong180903.driver.model.Message

data class GetHistoryChatMessagesResponse (
    val message : String? = null,
    val messages : List<Message>
)