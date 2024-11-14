package com.VVADU.test123

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.VVADU.test123.databinding.ActivityRegister2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegister2Binding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.registerBtn.setOnClickListener {
            binding.progressBar.isVisible = true
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val username = binding.usernameInput.text.toString() // Ambil username dari input
            val role = binding.roleInput.text.toString() // Ambil role dari input

            if (!validateInput(email, "Enter Email") ||
                !validateInput(password, "Enter Password") ||
                !validateInput(username, "Enter Username") ||
                !validateInput(role, "Enter Role")
            ) {
                binding.progressBar.isVisible = false
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.progressBar.isVisible = false
                    if (task.isSuccessful) {
                        // Ambil UID dari pengguna yang baru dibuat
                        val userId = auth.currentUser?.uid

                        // Buat objek data untuk disimpan
                        val userData = hashMapOf(
                            "email" to email,
                            "username" to username,
                            "role" to role
                        )

                        // Simpan data pengguna ke Firestore
                        userId?.let {
                            firestore.collection("users").document(it)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Account Created and Data Saved.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this,
                                        "Error saving data: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    } else {
                        // Tangani kesalahan pendaftaran
                        val errorMessage = task.exception?.message
                        if (errorMessage != null) {
                            if (errorMessage.contains("email address is already in use")) {
                                Toast.makeText(
                                    this,
                                    "Email already in use. Please use a different email.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Registration failed: $errorMessage",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Registration failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

        }

        // Button Navigasi
        binding.textLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInput(input: String, errorMessage: String): Boolean {
        return if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
}
