package com.VVADU.test123

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.VVADU.test123.databinding.ActivityAddKameraBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.math.log


class addKamera : AppCompatActivity() {
    private val binding by lazy {ActivityAddKameraBinding.inflate(layoutInflater)}
    private val selectedImages = mutableListOf<Uri>()
    private val productStorage = Firebase.storage.reference
    private val firestore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.saveButton.setOnClickListener{
            saveKamera()
        }
        binding.backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }



    private fun saveKamera() {
        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val offerPriceTag = binding.offerPercentage.text.toString().trim()
        val description = binding.edDescription.text.toString().trim()
        val imagesIn = binding.namaKamera.text.toString().trim()
        // Gambar tidak digunakan, jadi kita tidak perlu menangani gambar atau byte arrays

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showLoading()  // Menampilkan loading sebelum proses
            }

            try {
                // Data kamera tanpa gambar
                val kamera = Kamera(
                    name,
                    category,
                    price,
                    if (offerPriceTag.isEmpty()) null else offerPriceTag.toFloat(),
                    if (description.isEmpty()) null else description,
                    imagesIn

                    // Daftar gambar kosong karena tidak ada gambar yang diunggah
                )

                // Menyimpan data kamera ke Firestore
                firestore.collection("Kameras").add(kamera).addOnSuccessListener {
                        hideLoading()  // Menampilkan loading sebelum prose
                        Toast.makeText(
                            this@addKamera,
                            "Data Kamera Sudah Berhasil Disimpan",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@addKamera, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                }.addOnFailureListener {
                        hideLoading()  // Menyembunyikan loading jika gagal
                    Log.e("Error", it.message.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    hideLoading()  // Menyembunyikan loading jika terjadi error
                }
            }
        }
    }


    private fun hideLoading() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImages.forEach{
            val stream = ByteArrayOutputStream()
            val imageBmp = MediaStore.Images.Media.getBitmap(contentResolver,it)
            if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)){
                imagesByteArray.add(stream.toByteArray())
            }
        }
        return imagesByteArray
    }

    private fun validateInformation(): Boolean{
        if(binding.edPrice.text.toString().trim().isEmpty())
            return false
        if(binding.edName.text.toString().trim().isEmpty())
            return false
        if(binding.edCategory.text.toString().trim().isEmpty())
            return false
        if(selectedImages.isEmpty())
            return false
        return true
    }
}