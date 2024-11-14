package com.VVADU.test123

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            gotoMainActivity()
        }, 3000L)
//        }

    }
    private fun gotoMainActivity(){
        val it = Intent(this, MainActivity::class.java)
            startActivity(it)
    }
}