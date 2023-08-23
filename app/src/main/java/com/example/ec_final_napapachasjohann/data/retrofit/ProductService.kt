package com.example.ec_final_napapachasjohann.data.retrofit

import com.example.ec_final_napapachasjohann.model.Products
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    suspend fun getProducts(): List<Products>
}