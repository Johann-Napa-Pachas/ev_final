package com.example.ec_final_napapachasjohann.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "categorias")
@Parcelize
data class Category(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String,
    val creationAt: String,
    val updatedAt: String
): Parcelable