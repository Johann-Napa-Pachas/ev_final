package com.example.ec_final_napapachasjohann

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ec_final_napapachasjohann.databinding.ItemProductsBinding
import com.example.ec_final_napapachasjohann.model.Products

class ProductListAdapter(var products: List<Products>, val onClick: (Products) -> Unit): RecyclerView.Adapter<ProductVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val binding = ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductVH(binding, onClick)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        holder.bind(products.get(position))
    }

    fun updateProducts(newProducts: List<Products>) {
        products = newProducts
        notifyDataSetChanged()
    }


}

class ProductVH(private val binding: ItemProductsBinding, val onClick: (Products) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(products: Products){
        Glide.with(binding.root)
            .load(products.images[0])
            .into(binding.imageView)
        binding.txtIdProd.text = "ID: ${products.id}"
        binding.txtTitle.text = "Producto: ${products.title}"
        binding.txtPrice.text = "Precio: ${products.price}"
        binding.txtDescription.text = "Descripción: ${products.description}"
        binding.txtNomCat.text = "Categoría: ${products.category.name}"

        binding.root.setOnClickListener {
            onClick(products)
        }
    }
}