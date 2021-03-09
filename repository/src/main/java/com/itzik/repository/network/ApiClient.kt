package com.itzik.repository.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient(baseUrl: String): IApiClient<APIItemInterface?> {

    private var retrofit: Retrofit? = null

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    override fun createApi(): APIItemInterface? {
      return retrofit?.create(APIItemInterface::class.java)
    }



    companion object {

        private  var  HOLDER: ApiClient? = null
        private val   BASE_URL = "https://reqres.in"

        @Synchronized
        fun  getInstance(): ApiClient? {
            if(HOLDER == null) {
                HOLDER = ApiClient(BASE_URL)
            }
            return HOLDER
        }
    }


}