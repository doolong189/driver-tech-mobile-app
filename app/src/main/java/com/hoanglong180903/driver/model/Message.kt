package com.hoanglong180903.driver.model

data class Message (
    val _id : String,
    val messageId : String,
    val senderId : UserInfo,
    val receiverId : UserInfo,
    val chats : List<Chat>,
    val lastMsg : String,
    val lastMsgTime : String
)