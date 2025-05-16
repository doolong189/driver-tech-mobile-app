package com.hoanglong180903.driver.data.usecase

import com.hoanglong180903.driver.data.remote.RetrofitInstance
import com.hoanglong180903.driver.data.requestmodel.GetHistoryChatMessagesRequest


class ChatMessageRepository {
    suspend fun getHistoryChatMessages(request : GetHistoryChatMessagesRequest) = RetrofitInstance.api.getHistoryChatMessages(request)
}