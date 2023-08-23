package com.example.ec_final_napapachasjohann.data.repository

import com.example.ec_final_napapachasjohann.data.db.ProductsDao
import com.example.ec_final_napapachasjohann.data.response.ApiResponse
import com.example.ec_final_napapachasjohann.data.retrofit.RetrofitInstance
import com.example.ec_final_napapachasjohann.model.Products
import java.lang.Exception

class ProductsRepository(val productsDao: ProductsDao? = null) {
    suspend fun getProducts():ApiResponse<List<Products>>{
        return try {
            val response = RetrofitInstance.getProductService().getProducts()
            ApiResponse.Success(response)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    suspend fun addFavoritos(products: Products){
        productsDao?.let{
            it.addFavoritos(products)
        }
    }

    suspend fun deleteFavorito(products: Products) {
        productsDao?.let {
            it.deleteFavorito(products)
        }
    }

    fun getFavoritos(): List<Products>{
        productsDao?.let{
            return it.getFavoritos()
        }?: kotlin.run {
            return listOf()
        }
    }



}