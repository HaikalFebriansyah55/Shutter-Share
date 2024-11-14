package com.VVADU.test123

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class exposureFragment : Fragment(R.layout.fragment_exposure) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan referensi spinner, input aperture, input ISO, tombol hitung, dan text view hasil
        val spinner: Spinner = view.findViewById(R.id.kondisi_spinner)
        val apertureInput: EditText = view.findViewById(R.id.aperture_input)
        val isoInput: EditText = view.findViewById(R.id.iso_input)
        val hitungButton: Button = view.findViewById(R.id.hitung_button)
        val hasilShutterSpeed: TextView = view.findViewById(R.id.hasil_shutter_speed)

        // Membuat ArrayAdapter untuk kondisi pencahayaan (gelap, sedang, cerah)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.kondisi_array, // Array kondisi pencahayaan (gelap, sedang, cerah)
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Menangani klik tombol untuk menghitung shutter speed
        hitungButton.setOnClickListener {
            val kondisi = spinner.selectedItem.toString()
            val aperture = apertureInput.text.toString().toFloatOrNull()
            val iso = isoInput.text.toString().toIntOrNull()

            if (aperture != null && iso != null) {
                val shutterSpeed = calculateShutterSpeed(kondisi, aperture, iso)
                hasilShutterSpeed.text = "Shutter Speed: 1/$shutterSpeed"
            } else {
                Toast.makeText(requireContext(), "Masukkan semua data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateShutterSpeed(kondisi: String, aperture: Float, iso: Int): Int {
        // Base shutter speed untuk kondisi cerah
        val baseShutterSpeed = 100f

        // Tentukan faktor kondisi pencahayaan (pastikan semua faktor adalah float)
        val kondisiFactor = when (kondisi) {
            "Gelap" -> 0.5f  // Faktor pengurangan di kondisi gelap
            "Sedang" -> 1f  // Faktor standar untuk kondisi sedang
            "Cerah" -> 2f  // Faktor pengurangan untuk kondisi cerah
            else -> 1f  // Default jika kondisi tidak terdefinisi
        }

        // Pengecekan untuk memastikan nilai aperture dan ISO tidak bernilai nol atau tidak valid
        if (aperture <= 0 || iso <= 0) {
            // Menampilkan pesan kesalahan jika aperture atau ISO tidak valid
            Toast.makeText(requireContext(), "Aperture dan ISO harus lebih besar dari 0", Toast.LENGTH_SHORT).show()
            return 0
        }

        // Menghitung shutter speed
        val denominator = aperture * iso.toFloat() * kondisiFactor

        // Pengecekan untuk memastikan denominator tidak menjadi nol atau sangat kecil
        if (denominator == 0f) {
            // Menampilkan pesan kesalahan jika denominator nol
            Toast.makeText(requireContext(), "Denominator tidak valid, periksa input aperture dan ISO", Toast.LENGTH_SHORT).show()
            return 0
        }

        // Menghitung shutter speed, pastikan denominator tidak nol
        val shutterSpeed = denominator

        // Mengonversi hasil menjadi integer dan mengembalikannya
        return shutterSpeed.toInt()
    }



}
