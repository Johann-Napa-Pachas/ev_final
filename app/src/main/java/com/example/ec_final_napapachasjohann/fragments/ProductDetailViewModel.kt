package com.example.ec_final_napapachasjohann.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ec_final_napapachasjohann.data.db.ProductsDataBase
import com.example.ec_final_napapachasjohann.data.repository.ProductsRepository
import com.example.ec_final_napapachasjohann.model.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ProductsRepository
    init {
        val db = ProductsDataBase.getDataBase(application)
        repository = ProductsRepository(db.productsDao())
    }
    fun addFavorite(products: Products){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavoritos(products)
        }
    }

    fun deleteFavorito(products: Products) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorito(products)
        }
    }
}