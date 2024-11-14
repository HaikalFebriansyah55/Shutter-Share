package com.VVADU.test123

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var param1: String? = null
    private var param2: String? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewKameraAdapter: RecyclerViewKameraAdapter? = null
    private var kameraList = mutableListOf<kameraHome>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Ambil data kamera dari Firestore

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kameraList = ArrayList()
        recyclerView = view.findViewById<RecyclerView>(R.id.rvkameraList)!!
        recyclerViewKameraAdapter =
            RecyclerViewKameraAdapter(getFragment = this@HomeFragment, kameraList)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = recyclerViewKameraAdapter
        fetchKameras()


    }
    @SuppressLint("NotifyDataSetChanged")
    private fun fetchKameras() {
        firestore.collection("Kameras")
            .get()
            .addOnSuccessListener { querySnapshot ->
                kameraList.clear()
                for (document in querySnapshot.documents) {
                    val name = document.getString("name") ?: ""
                    val category = document.getString("category") ?: ""
                    val price = document.getString("price")?.toFloatOrNull() ?: 0f
                    val offerPercentage = document.getString("offerPercentage")?.toFloatOrNull() ?: 0f
                    val description = document.getString("description") ?: ""
                    val image = document.getString("image") ?: ""
                    kameraList.add(kameraHome(name, category, price, offerPercentage, description, image))
                }
                recyclerViewKameraAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error getting camera data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }



//    @SuppressLint("NotifyDataSetChanged")
//    private fun prepareKameraListData() {
//        var kamera = Kamera(
//            "Sony A7", "Sony", 90000F, 0F,"""
//Sony A7 adalah kamera mirrorless full-frame yang dikenal karena kualitas gambar yang tinggi dan performa yang luar biasa dalam berbagai kondisi pencahayaan. Dikenal dengan sensor CMOS Exmor 35mm yang memungkinkan resolusi tinggi dan rentang dinamis yang luas, A7 menawarkan kemampuan autofocus cepat dan akurat, serta video 4K. Desainnya yang kompak dan ringan membuatnya mudah dibawa, sementara berbagai lensa yang kompatibel memperluas kreativitas pengguna. Kamera ini sangat populer di kalangan fotografer profesional dan penggemar serius berkat kombinasi teknologi canggih dan portabilitas.""", R.drawable.sony_a7
//        )
//        kameraList.add(kamera)
//        Log.d("HomeFragment", "Added: ${kamera.name}")
//        kamera = Kamera(
//            "Sony A9", "Sony", 150000F, 0F,"""
//Sony A7 adalah kamera mirrorless full-frame yang dikenal karena kualitas gambar yang tinggi dan performa yang luar biasa dalam berbagai kondisi pencahayaan. Dikenal dengan sensor CMOS Exmor 35mm yang memungkinkan resolusi tinggi dan rentang dinamis yang luas, A7 menawarkan kemampuan autofocus cepat dan akurat, serta video 4K. Desainnya yang kompak dan ringan membuatnya mudah dibawa, sementara berbagai lensa yang kompatibel memperluas kreativitas pengguna. Kamera ini sangat populer di kalangan fotografer profesional dan penggemar serius berkat kombinasi teknologi canggih dan portabilitas.""",
//            R.drawable.sony_a7
//        )
//        kameraList.add(kamera)
//        Log.d("HomeFragment", "Added: ${kamera.name}")
//        kamera = Kamera(
//            "Sony A9", "Sony", 150000F, 0F,"""
//Sony A7 adalah kamera mirrorless full-frame yang dikenal karena kualitas gambar yang tinggi dan performa yang luar biasa dalam berbagai kondisi pencahayaan. Dikenal dengan sensor CMOS Exmor 35mm yang memungkinkan resolusi tinggi dan rentang dinamis yang luas, A7 menawarkan kemampuan autofocus cepat dan akurat, serta video 4K. Desainnya yang kompak dan ringan membuatnya mudah dibawa, sementara berbagai lensa yang kompatibel memperluas kreativitas pengguna. Kamera ini sangat populer di kalangan fotografer profesional dan penggemar serius berkat kombinasi teknologi canggih dan portabilitas.""",
//            R.drawable.canon_m50
//        )
//        kameraList.add(kamera)
//        Log.d("HomeFragment", "Added: ${kamera.name}")
//        kamera = Kamera(
//           "Sony A9", "Sony", 150000F, 0F,"""
//Sony A7 adalah kamera mirrorless full-frame yang dikenal karena kualitas gambar yang tinggi dan performa yang luar biasa dalam berbagai kondisi pencahayaan. Dikenal dengan sensor CMOS Exmor 35mm yang memungkinkan resolusi tinggi dan rentang dinamis yang luas, A7 menawarkan kemampuan autofocus cepat dan akurat, serta video 4K. Desainnya yang kompak dan ringan membuatnya mudah dibawa, sementara berbagai lensa yang kompatibel memperluas kreativitas pengguna. Kamera ini sangat populer di kalangan fotografer profesional dan penggemar serius berkat kombinasi teknologi canggih dan portabilitas.""",
//            R.drawable.canon_m100
//        )
//        kameraList.add(kamera)
//        Log.d("HomeFragment", "Added: ${kamera.name}")
//
//        recyclerViewKameraAdapter!!.notifyDataSetChanged()
//    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}