package com.example.ec_final_napapachasjohann.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ec_final_napapachasjohann.data.db.ProductsDataBase
import com.example.ec_final_napapachasjohann.data.repository.ProductsRepository
import com.example.ec_final_napapachasjohann.model.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductFavoriteViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ProductsRepository
    private val _favorites:MutableLiveData<List<Products>> = MutableLiveData()
    val favorites: LiveData<List<Products>> = _favorites
    init {
        val db = ProductsDataBase.getDataBase(application)
        repository = ProductsRepository(db.productsDao())
    }

    fun getFavoritos() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getFavoritos()
            _favorites.postValue(data)
        }
    }

    suspend fun deleteFavorito(products: Products) {
        withContext(Dispatchers.IO) {
            repository.deleteFavorito(products)
        }
    }
}