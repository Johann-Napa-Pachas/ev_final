package com.example.ec_final_napapachasjohann.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ec_final_napapachasjohann.databinding.ActivityAddProductBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var openCameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var openGalleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var firestore: FirebaseFirestore
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firestore = Firebase.firestore

        binding.btnFoto.setOnClickListener {
            if (permissionValidated(Manifest.permission.CAMERA)) {
                openCamera()
            }
        }

        binding.btnGalery.setOnClickListener {
            if (permissionValidated(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery()
            }
        }

        openCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photo: Bitmap = result.data?.extras?.get("data") as Bitmap
                binding.photo.setImageBitmap(photo)
                selectedImageUri = getImageUriFromBitmap(photo)
            }
        }

        openGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImage: Uri? = result.data?.data
                if (selectedImage != null) {
                    val confirmationDialog = AlertDialog.Builder(this)
                        .setTitle("Confirmar Foto")
                        .setMessage("¿Deseas usar esta foto?")
                        .setPositiveButton("Sí") { _, _ ->
                            binding.photo.setImageURI(selectedImage)
                            selectedImageUri = selectedImage
                        }
                        .setNegativeButton("No") { _, _ ->
                        }
                        .create()

                    confirmationDialog.show()
                }
            }
        }

        binding.btnRegistrar.setOnClickListener {
            val title: String = binding.tilTitle.editText?.text.toString()
            val price: String = binding.tilPrice.editText?.text.toString()
            val descripcion: String = binding.tilDescription.editText?.text.toString()
            val category: String = binding.tilCategory.editText?.text.toString()

            val imageUri = selectedImageUri

            if (title.isNotEmpty() && price.isNotEmpty() && descripcion.isNotEmpty() && category.isNotEmpty() && imageUri != null) {

                val storage = Firebase.storage
                val storageRef = storage.reference
                val imageFileName = "product_image_${System.currentTimeMillis()}.jpg"
                val imageRef = storageRef.child("images/$imageFileName")

                val imageBitmap = getImageBitmapFromUri(imageUri)

                if (imageBitmap != null) {
                    val baos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val imageData: ByteArray = baos.toByteArray()

                    val uploadTask = imageRef.putBytes(imageData)
                    uploadTask.addOnSuccessListener { taskSnapshot ->

                        imageRef.downloadUrl.addOnCompleteListener { downloadUrlTask ->
                            if (downloadUrlTask.isSuccessful) {
                                val downloadUrl = downloadUrlTask.result.toString()

                                val product = hashMapOf(
                                    "category" to category,
                                    "title" to title,
                                    "price" to price.toInt(),
                                    "descripcion" to descripcion,
                                    "creationAt" to Timestamp.now(),
                                    "updateAt" to Timestamp.now(),
                                    "images" to downloadUrl
                                )

                                firestore.collection("product")
                                    .add(product)
                                    .addOnSuccessListener { documentReference ->

                                        val productId = documentReference.id

                                        val updateFields = hashMapOf<String, Any>(
                                            "updateAt" to Timestamp.now()
                                        )
                                        firestore.collection("product")
                                            .document(productId)
                                            .update(updateFields)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "Producto agregado correctamente con id: $productId", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "No se pudo agregar el producto.", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Ocurrió un error.", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Vuelva a intentarlo.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    uploadTask.addOnFailureListener {
                        Toast.makeText(this, "Error 404.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun permissionValidated(permission: String): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1000)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions[0] == Manifest.permission.CAMERA) {
                        openCamera()
                    } else if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        openGallery()
                    }
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        openCameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        openGalleryLauncher.launch(intent)
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun getImageBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}