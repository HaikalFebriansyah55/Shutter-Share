package com.VVADU.test123

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.VVADU.test123.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.blogspot.atifsoftwares.animatoolib.R.anim as Anim
import androidx.appcompat.widget.Toolbar

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    var toolName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Set custom action bar
        setSupportActionBar(binding.toolbar)
          // Ubah judul sesuai kebutuhan
        binding.toAddKamera.setOnClickListener {
            intent = Intent(this, addKamera::class.java)
            startActivity(intent)
        }

        // Set Edge-to-Edge layout jika diperlukan
        enableEdgeToEdge()
        toolName = "Home"
        replaceFragment(HomeFragment(), R.anim.slide_left, R.anim.slide_right, toolName)

        binding.menuNav.setOnItemSelectedListener {
            val currentFragment = getCurrentFragment()

            when (it.itemId) {
                R.id.home_nav -> {
                    toolName = "Home"
                    if (currentFragment is AccountFragment || currentFragment is ChartFragment || currentFragment is exposureFragment) {
                        replaceFragment(HomeFragment(), Anim.animate_swipe_left_enter, Anim.abc_fade_out, toolName)
                    }
                }
                R.id.user_nav -> {
                    toolName = "Account"
                    if (currentFragment is HomeFragment || currentFragment is exposureFragment || currentFragment is ChartFragment) {
                        replaceFragment(
                            AccountFragment(),
                            Anim.animate_swipe_right_enter,
                            Anim.abc_fade_out,
                            toolName
                        )
                    }
                }
                R.id.chart_nav -> {
                    toolName = "Chart"
                    if (currentFragment is HomeFragment) {
                        replaceFragment(ChartFragment(), Anim.animate_swipe_right_enter, Anim.abc_fade_out, toolName)
                    }else if(currentFragment is AccountFragment || currentFragment is exposureFragment){
                        replaceFragment(ChartFragment(), Anim.animate_swipe_left_enter, Anim.abc_fade_out, toolName)
                    }
                }
                R.id.exp_nav -> {
                    toolName = "Exposure"
                    if (currentFragment is HomeFragment) {
                        replaceFragment(exposureFragment(), Anim.animate_swipe_left_enter, Anim.abc_fade_out, toolName)
                    }else if(currentFragment is AccountFragment || currentFragment is ChartFragment){
                        replaceFragment(exposureFragment(), Anim.animate_swipe_right_enter, Anim.abc_fade_out, toolName)
                    }
                }
                else -> { }
            }
            true
        }
    }

    // Fungsi untuk mendapatkan fragment yang aktif saat ini
    private fun getCurrentFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(R.id.frame_layout)
    }

    // Fungsi untuk mengganti fragment dengan animasi
    private fun replaceFragment(fragment: Fragment, enterAnim: Int, exitAnim: Int, toolName:String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // Atur animasi berdasarkan parameter
        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim)

        // Ganti fragment
        supportActionBar?.title = toolName
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
