package com.VVADU.test123

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.VVADU.test123.databinding.ActivityLogin2Binding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLogin2Binding

    override fun onStart() {
        super.onStart()
        // Cek apakah pengguna sudah masuk
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Jika sudah masuk, langsung ke HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (!validateInput(email, "Enter Email") ||
                !validateInput(password, "Enter Password")) {
                return@setOnClickListener
            }

            binding.progressBar.isVisible = true // Pindah ke sini untuk memastikan hanya muncul jika validasi sukses

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.progressBar.isVisible = false
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Login Successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Jika login gagal, tampilkan pesan kesalahan
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.textRegist.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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
