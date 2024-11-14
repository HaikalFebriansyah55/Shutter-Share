package com.VVADU.test123

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.VVADU.test123.databinding.ActivityDetailBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DetailActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Detail Product"
        // Set Edge-to-Edge layout jika diperlukan
        enableEdgeToEdge()

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Ambil data yang dikirim dari RecyclerView
        val title = intent.getStringExtra("name")
        val image = intent.getIntExtra("image", 0)
        val price = intent.getFloatExtra("price", 0F).toString()
        val description = intent.getStringExtra("description")

        // Set data ke view
        binding.detailTitle.text = title
        binding.detailImage.setImageResource(image)
        binding.detailDesk.text = description // Jika ada deskripsi
        binding.detaiCk.text = title
        binding.buttonChekout.setOnClickListener {
            val modelKamera = binding.detailTitle.text.toString()
            val imageKamera = resources.getResourceEntryName(image)
            // Ambil UID dari pengguna yang sedang login
            val userId = auth.currentUser?.uid

            // Buat objek data untuk disimpan
            val kameraData = hashMapOf(
                "model" to modelKamera,
                "image" to imageKamera,
                "price" to price

            )

            // Simpan data kamera ke koleksi "cameras" di bawah dokumen pengguna
            userId?.let {
                firestore.collection("users").document(it).collection("cameras")
                    .add(kameraData) // Gunakan add() untuk membuat dokumen baru di dalam koleksi
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Camera Data Saved.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Error saving data: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }


    }
}
