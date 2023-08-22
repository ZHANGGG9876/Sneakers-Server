package com.sneakers_shop_server

import com.sneakers_shop_server.retrofit.PushNotification
import com.sneakers_shop_server.retrofit.ResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("Authorization: key=" + Constants.SERVER_KEY, "Content-Type:" + Constants.CONTENT_TYPE)
    @POST("fcm/send")
    fun postNotification(@Body data: PushNotification?): Call<ResponseModel?>?
}