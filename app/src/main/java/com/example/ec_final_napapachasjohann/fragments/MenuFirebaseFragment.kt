package com.example.ec_final_napapachasjohann.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ec_final_napapachasjohann.ProductFirebaseListAdapter
import com.example.ec_final_napapachasjohann.R
import com.example.ec_final_napapachasjohann.model.ProductsFirebase
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


class MenuFirebaseFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productsArrayList: ArrayList<ProductsFirebase>
    private lateinit var productFirebaseListAdapter: ProductFirebaseListAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_menu_firebase, container, false)
        recyclerView = rootView.findViewById(R.id.productsFirebaseList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        productsArrayList = arrayListOf()

        productFirebaseListAdapter = ProductFirebaseListAdapter(productsArrayList)
        recyclerView.adapter = productFirebaseListAdapter

        EventChangeListener()

        return rootView
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("product")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    val productsList = ArrayList<ProductsFirebase>()

                    for (document in value!!.documents) {
                        val product = document.toObject(ProductsFirebase::class.java)
                        product?.id = document.id
                        product?.let { productsList.add(it) }
                    }

                    productFirebaseListAdapter.updateList(productsList)
                }
            })
    }
}
