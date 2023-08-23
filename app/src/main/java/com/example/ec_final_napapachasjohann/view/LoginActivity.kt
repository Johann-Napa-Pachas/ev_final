package com.example.ec_final_napapachasjohann.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.example.ec_final_napapachasjohann.R
import com.example.ec_final_napapachasjohann.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        sharedPreferences = getSharedPreferences(SESSION_PREFERENCE, MODE_PRIVATE)
        firebaseAuth = Firebase.auth
        googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    signInFirebaseWithGoogle(account.idToken)
                }catch (e:Exception){
                    Toast.makeText(this,"Ocurrió un error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signInFirebaseWithGoogle(idToken: String?) {
        val authCredential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(authCredential)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser = firebaseAuth.currentUser!!
                    sharedPreferences.edit().apply{
                        putString(EMAIL, user.email)
                            .apply()
                    }
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

    private fun setupViews(){
        binding.email.editText?.addTextChangedListener { text ->
            binding.btnLogin.isEnabled = validateInputs(text.toString(),binding.password.editText?.text.toString())
        }

        binding.password.editText?.addTextChangedListener { text ->
            binding.btnLogin.isEnabled = validateInputs(binding.email.editText?.text.toString(), text.toString())
        }

        binding.btnLogin.setOnClickListener {
            signInWithEmailPassword()
        }

        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnSignUp.setOnClickListener {
            signUpWithEmailPassword()
        }
    }

    private fun signUpWithEmailPassword() {
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        if (validateInputs(email, password)){
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Toast.makeText(this,"Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"El usuario no pudo ser creado", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(this,"Credenciales no válidas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithEmailPassword() {
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            signInFirebaseWithEmail(email, password)
        } else {
            Toast.makeText(this, "Por favor, ingrese su correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInFirebaseWithEmail(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser = firebaseAuth.currentUser!!
                    sharedPreferences.edit().apply {
                        putString(EMAIL, user.email)
                        apply()
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error al iniciar sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.web_client_id))
            .requestEmail().build()
        val client: GoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)
        val intent = client.signInIntent
        googleLauncher.launch(intent)
    }

    private fun validateInputs(email:String, password:String): Boolean{
        val isEmailOk = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordOk = password.length >= 6
        return isPasswordOk && isEmailOk
    }

    companion object{
        const val SESSION_PREFERENCE: String = "SESSION_PREFERENCES"
        const val EMAIL: String = "EMAIL"
    }
}