package com.VVADU.test123

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChartFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var cameraAdapter: CameraAdapter
    private val cameraList = mutableListOf<Camera>()
    private lateinit var totalHargaTextView: TextView // Tambahkan TextView untuk total harga

    private lateinit var radioGroupDiscount: RadioGroup

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chart, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.divider, null))
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize radio group for discount
        radioGroupDiscount = view.findViewById(R.id.radioGroupDiscount)

        // Initialize TextView for total price
        totalHargaTextView = view.findViewById(R.id.totalHarga)

        // Fetch camera data
        fetchCameras()

        // Set up listener for discount radio buttons
        radioGroupDiscount.setOnCheckedChangeListener { _, _ ->
            fetchCameras() // Re-fetch cameras when the discount selection changes
        }

        return view
    }

    private fun fetchCameras() {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it).collection("cameras")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    cameraList.clear()
                    val discountPercentage = getDiscountPercentage() // Get selected discount

                    for (document in querySnapshot.documents) {
                        val model = document.getString("model") ?: ""
                        val image = document.getString("image") ?: ""
                        val price = document.getString("price")?.toDoubleOrNull() ?: 0.0

                        // Apply the discount to the price
                        val discountedPrice = applyDiscount(price, discountPercentage)

                        // Add the camera with the discounted price
                        cameraList.add(Camera(model, image, discountedPrice.toString()))
                    }

                    // Initialize the adapter with a listener for total price calculation
                    cameraAdapter = CameraAdapter(cameraList) { totalPrice ->
                        // Update the total price TextView
                        totalHargaTextView.text = String.format("Rp.%.2f", totalPrice)
                    }
                    recyclerView.adapter = cameraAdapter

                    // Calculate the total price after the adapter is set
                    cameraAdapter.calculateTotalPrice()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error getting camera data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Get the selected discount percentage
    private fun getDiscountPercentage(): Double {
        return when (radioGroupDiscount.checkedRadioButtonId) {
            R.id.radioButton25 -> 25.0
            R.id.radioButton30 -> 30.0
            else -> 0.0
        }
    }

    // Apply the discount to the original price
    private fun applyDiscount(price: Double, discountPercentage: Double): Double {
        return price - (price * (discountPercentage / 100))
    }
}




