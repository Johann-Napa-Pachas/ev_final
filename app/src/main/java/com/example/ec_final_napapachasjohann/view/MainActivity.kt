package com.example.ec_final_napapachasjohann.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ec_final_napapachasjohann.ProductFirebaseListAdapter
import com.example.ec_final_napapachasjohann.R
import com.example.ec_final_napapachasjohann.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.products) as NavHostFragment
        val navController = navHostFragment.navController
        binding.btnMenu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.menuFirebaseFragment) {
                binding.btnEdit.visibility = View.VISIBLE
            } else {
                binding.btnEdit.visibility = View.GONE
            }
        }
    }
}