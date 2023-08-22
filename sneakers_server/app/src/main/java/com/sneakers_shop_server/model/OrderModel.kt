package com.sneakers_shop_server.model

data class OrderModel (
    var id : String? = null,
    val userName: String? = null,
    val userEmail : String? = null,
    val userPhone :  Int? = 0,
    val sneakers : List<SneakerOrderModel>? = null,
    val total : Double? = null,
    var status : Status = Status.PROCESSING,
    val date : String? = null,
    val address : String? = null,
        )