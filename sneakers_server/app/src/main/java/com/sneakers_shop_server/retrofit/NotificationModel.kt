package com.sneakers_shop_server.retrofit

data class NotificationModel(
    var title : String,
    var body : String,
    private  val image : String? = null
)