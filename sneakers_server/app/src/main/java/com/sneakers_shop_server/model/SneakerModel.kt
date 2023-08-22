package com.sneakers_shop_server.model

data class SneakerModel(
    var id : String? =null,
    var brand: String? = null,
    var description: String? = null,
    var name: String? = null,
    var size: List<Double>? = null,
    var price: Double? = null,
    var gender: String? = null,
    var image: String? = null,
    var quantity : Int? = null
)
