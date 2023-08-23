package com.example.ec_final_napapachasjohann.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "productos")
@Parcelize
data class Products(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val images: List<String>,
    val creationAt: String,
    val updatedAt: String,
    var isFavorite: Boolean = false,
    val category: Category
): Parcelable