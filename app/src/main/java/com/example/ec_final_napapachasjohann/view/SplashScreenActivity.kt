package com.example.ec_final_napapachasjohann.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.ec_final_napapachasjohann.R
import com.example.ec_final_napapachasjohann.databinding.ActivitySplashScreenBinding
import android.view.animation.AnimationUtils
import android.view.animation.Animation

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(LoginActivity.SESSION_PREFERENCE, MODE_PRIVATE)

        val fadeInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.animation_in)
        val fadeOutAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.animation_out)

        val logo = binding.logo
        val loadingTextView = binding.loading

        logo.startAnimation(fadeInAnimation)
        loadingTextView.startAnimation(fadeInAnimation)

        Handler().postDelayed({
            logo.startAnimation(fadeOutAnimation)
            loadingTextView.startAnimation(fadeOutAnimation)

            fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    logo.visibility = View.INVISIBLE
                    loadingTextView.visibility = View.INVISIBLE

                    Handler().postDelayed({
                        val email: String = sharedPreferences.getString(LoginActivity.EMAIL, "") ?: ""
                        val intent = if (email.isNotEmpty()) {
                            Intent(this@SplashScreenActivity, MainActivity::class.java)
                        } else {
                            Intent(this@SplashScreenActivity, LoginActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0)
                    }, 0)
                }
            })

        }, 3500)
    }
}