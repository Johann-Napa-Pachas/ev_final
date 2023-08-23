package com.example.ec_final_napapachasjohann.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    https://api.escuelajs.co/api/v1/products
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getProductService(): ProductService = retrofit.create(ProductService::class.java)
}