package com.example.ec_final_napapachasjohann.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.ec_final_napapachasjohann.R
import com.example.ec_final_napapachasjohann.databinding.FragmentFavoritosBinding
import com.example.ec_final_napapachasjohann.databinding.FragmentLogoutBinding
import com.example.ec_final_napapachasjohann.view.LoginActivity
import com.example.ec_final_napapachasjohann.view.SplashScreenActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutFragment : Fragment() {
    private lateinit var binding: FragmentLogoutBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(
            LoginActivity.SESSION_PREFERENCE,
            AppCompatActivity.MODE_PRIVATE
        )
        firebaseAuth = Firebase.auth
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener{
            binding.btnLogout.text = "CERRANDO SESIÓN\nESPERE UN MOMENTO..."

            firebaseAuth.signOut()
            sharedPreferences.edit().apply {
                putString(LoginActivity.EMAIL, "")
                apply()
            }

            Handler().postDelayed({
                // Navega a SplashScreenActivity
                val intent = Intent(requireActivity(), SplashScreenActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            }, 2000) // Espera 2 segundos antes de iniciar SplashScreenActivity
        }
    }

}