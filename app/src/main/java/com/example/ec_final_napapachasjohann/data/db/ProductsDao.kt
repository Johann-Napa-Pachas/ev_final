package com.example.ec_final_napapachasjohann.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ec_final_napapachasjohann.model.Products
import com.example.ec_final_napapachasjohann.model.ProductsFirebase

@Dao
interface ProductsDao {
    @Query("SELECT * FROM productos")
    fun getFavoritos(): List<Products>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoritos(products: Products)

    @Delete
    suspend fun deleteFavorito(products: Products)
}