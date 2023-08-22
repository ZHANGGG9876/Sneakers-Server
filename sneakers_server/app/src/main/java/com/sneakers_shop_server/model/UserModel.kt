package com.sneakers_shop_server.model

class UserModel(
    var username : String? = null,
    var password : String? = null,
    var phone_number :String? = null,
    var correo : String? = null,
    var direction: String? = null,
    var sneakerFav : List<SneakerModel>? = null,
    var sneakerBag : List<SneakerModel>? = null,
    var sneakerRes : List<SneakerModel>? = null,
    var sneakerOrd : List<SneakerModel>? = null
    )