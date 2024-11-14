package com.VVADU.test123

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class RecyclerViewKameraAdapter constructor(private val getFragment: HomeFragment, private val kameraList : List<kameraHome>): RecyclerView.Adapter<RecyclerViewKameraAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewKameraAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_kamer_list_item, parent, false) // Pastikan nama layout benar
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Retrieve the kamera object at the current position
        val kamera = kameraList[position]

        // Log binding information for debugging
        Log.d("RecyclerViewAdapter", "Binding item at position $position: ${kamera.name}")

        // Set the title and image for the holder
        holder.kameraTitle.text = kamera.name
        val resourceId = holder.itemView.context.resources.getIdentifier(kamera.image, "drawable", holder.itemView.context.packageName)
        holder.kameraImg.setImageResource(resourceId)
//        holder.kameraImg.setImageResource(kamera.image)
        // Set click listener
        holder.cardView.setOnClickListener {
            // Buat Intent untuk pindah ke DetailActivity
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            // Masukkan data kamera ke dalam Intent
            intent.putExtra("name", kamera.name)
            intent.putExtra("image", resourceId)
            intent.putExtra("price", kamera.price)
            intent.putExtra("description", kamera.description) // Mengirim deskripsi jika ada

            // Mulai DetailActivity
            holder.itemView.context.startActivity(intent)
            Animatoo.animateFade(holder.itemView.context)
        }
    }

    override fun getItemCount(): Int {
        return kameraList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val kameraTitle : TextView = itemView.findViewById(R.id.product_name)
        val kameraImg : ImageView = itemView.findViewById(R.id.product_img)
        val cardView : CardView = itemView.findViewById(R.id.card_view)


    }

}
data class kameraHome(val name: String, val category: String,val price: Float,val offerPercentage: Float, val description: String, val image: String)