package com.VVADU.test123

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.VVADU.test123.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentAccountBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val user = auth.currentUser

        if (user == null) {
            // Navigasi ke LoginActivity jika tidak ada user yang terautentikasi
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {
            // Menampilkan email pengguna
            binding.textUser.text = user.email

            // Mengambil username dari Firestore
            firestore.collection("users").document(user.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val username = document.getString("username")
                        binding.textUser.text = username // Misalkan Anda punya TextView dengan id textUsername
                    } else {
                        binding.textUser.text = "Username not found"
                    }
                }
                .addOnFailureListener { e ->
                    binding.textUser.text = "Error getting username: ${e.message}"
                }
        }

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
