package com.example.ec_final_napapachasjohann.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ec_final_napapachasjohann.databinding.FragmentDetailBinding
import com.example.ec_final_napapachasjohann.model.Products
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    val args: DetailFragmentArgs by navArgs()
    private lateinit var products: Products
    private lateinit var viewModel: ProductDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        products = args.products
        viewModel = ViewModelProvider(requireActivity()).get(ProductDetailViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.root)
            .load(products.images[0])
            .into(binding.imageView)
        binding.txtIdProd.text = "ID: ${products.id}"
        binding.txtTitle.text = "Producto: ${products.title}"
        binding.txtPrice.text = "Precio: ${products.price}"
        binding.txtDescription.text = "Descripción: ${products.description}"
        binding.txtCreacionEn.text = "Creado el: ${products.creationAt}"
        binding.txtActualizadoEn.text = "Creado el: ${products.updatedAt}"
        binding.txtNomCat.text = "Categoría: ${products.category.name}"

        binding.btnAddFavorite.apply {
            visibility = if (products.isFavorite) View.GONE else View.VISIBLE
        }

        binding.btnAddFavorite.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.addFavorite(products)
                products.isFavorite = true
                findNavController().navigateUp()
            }
        }

        binding.btnDeleteFavorite.apply {
            visibility = if (products.isFavorite) View.VISIBLE else View.GONE
        }

        binding.btnDeleteFavorite.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.deleteFavorito(products)
                products.isFavorite = false
                findNavController().navigateUp()
            }
        }
    }
}