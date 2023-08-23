package com.example.ec_final_napapachasjohann

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ec_final_napapachasjohann.model.ProductsFirebase

class ProductFirebaseListAdapter(private var productsList: ArrayList<ProductsFirebase>): RecyclerView.Adapter<ProductFirebaseListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById((R.id.txt_idProd))
        val title: TextView = itemView.findViewById(R.id.txt_title)
        val price: TextView = itemView.findViewById(R.id.txt_price)
        val descripcion: TextView = itemView.findViewById(R.id.txt_description)
        val category: TextView = itemView.findViewById(R.id.txt_nomCat)
        val images: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(product: ProductsFirebase) {
            id.text = "ID: ${product.id}"
            title.text = "Producto: ${product.title}"
            price.text = "Precio: ${product.price.toString()}"
            descripcion.text = "Descripción: ${product.descripcion}"
            category.text = "Categoría: ${product.category}"
            Glide.with(itemView.context)
                .load(product.images)
                .into(images)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        return MyViewHolder(itemView)
    }

    fun updateList(newList: ArrayList<ProductsFirebase>) {
        productsList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product: ProductsFirebase = productsList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}
