package com.example.ec_final_napapachasjohann.model

import com.google.firebase.Timestamp

data class ProductsFirebase(
    var id: String?= null,
    var title: String?= null,
    var price: Double?= null,
    var descripcion: String?= null,
    var images: String?= null,
    var creationAt: Timestamp? = null,
    var updateAt: Timestamp? = null,
    var isFavorite: Boolean = false,
    var category: String?= null
){
    constructor() : this(null)

    fun updateTimestamps() {
        val currentTime = Timestamp.now()
        if (creationAt == null) {
            creationAt = currentTime
        }
        updateAt = currentTime
    }
}
