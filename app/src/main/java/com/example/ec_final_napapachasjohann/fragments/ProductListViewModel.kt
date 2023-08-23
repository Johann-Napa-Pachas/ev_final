package com.example.ec_final_napapachasjohann.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ec_final_napapachasjohann.data.repository.ProductsRepository
import com.example.ec_final_napapachasjohann.data.response.ApiResponse
import com.example.ec_final_napapachasjohann.model.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel: ViewModel() {
    private val repository = ProductsRepository()
    val productList: MutableLiveData<List<Products>> = MutableLiveData()

    fun getProductFromService() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProducts()
            productList.postValue(when (response) {
                is ApiResponse.Success -> response.data
                else -> emptyList()
            })
        }
    }
}