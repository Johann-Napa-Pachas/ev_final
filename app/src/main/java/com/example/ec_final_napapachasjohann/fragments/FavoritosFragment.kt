package com.example.ec_final_napapachasjohann.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ec_final_napapachasjohann.ProductListAdapter
import com.example.ec_final_napapachasjohann.R
import com.example.ec_final_napapachasjohann.databinding.FragmentFavoritosBinding

class FavoritosFragment : Fragment() {
    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var viewModel: ProductFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ProductFavoriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ProductListAdapter(listOf()){products ->
            val favoriteDetailDirection = FavoritosFragmentDirections.actionFavoritosFragmentToDetailFragment(products)
            findNavController().navigate(favoriteDetailDirection)
        }

        binding.productsList.adapter = adapter
        binding.productsList.layoutManager = GridLayoutManager(requireContext(),1,
            RecyclerView.VERTICAL,false)
        viewModel.favorites.observe(requireActivity()) {
            adapter.products = it
            adapter.notifyDataSetChanged()
        }
        viewModel.getFavoritos()
    }
}