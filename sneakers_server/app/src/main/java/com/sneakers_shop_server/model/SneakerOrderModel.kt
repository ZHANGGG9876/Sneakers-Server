package com.sneakers_shop_server.model

data class SneakerOrderModel (
    val brand : String? = null,
    val name: String? = null,
    val description : String? = null,
    val size : Double? = null,
    val price : Double? = null,
    val totalPrice : Double? = null,
    val gender : String? = null,
    val quantity : Int? = null,
    val image : String? =null,
)