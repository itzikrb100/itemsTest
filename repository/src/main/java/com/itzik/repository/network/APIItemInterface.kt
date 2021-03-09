package com.itzik.repository.network

import retrofit2.Call
import retrofit2.http.GET




interface APIItemInterface {

    @GET("/api/unknown")
    fun doGetListItems(): Call<Item>
}