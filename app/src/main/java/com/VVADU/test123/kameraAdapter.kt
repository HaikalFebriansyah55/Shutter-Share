package com.VVADU.test123

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CameraAdapter(private val cameraList: List<Camera>, private val onTotalPriceCalculated: (Double) -> Unit) :
    RecyclerView.Adapter<CameraAdapter.CameraViewHolder>() {

    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cameraImage: ImageView = itemView.findViewById(R.id.cameraImage)
        val cameraName: TextView = itemView.findViewById(R.id.cameraName)
        val cameraPrice: TextView = itemView.findViewById(R.id.cameraPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chart_item_card, parent, false)
        return CameraViewHolder(view)
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
        val camera = cameraList[position]
        holder.cameraName.text = camera.model

        // Display the discounted price
        val price = camera.price.toDoubleOrNull() ?: 0.0
        holder.cameraPrice.text = String.format("Rp.%.2f", price)

        // Set the image for the camera
        val resourceId = holder.itemView.context.resources.getIdentifier(camera.image, "drawable", holder.itemView.context.packageName)
        holder.cameraImage.setImageResource(resourceId)
    }

    override fun getItemCount(): Int {
        return cameraList.size
    }

    // Calculate total price and notify the listener
    fun calculateTotalPrice(): Double {
        var totalPrice = 0.0
        cameraList.forEach {
            totalPrice += it.price.toDoubleOrNull() ?: 0.0
        }
        onTotalPriceCalculated(totalPrice)
        return totalPrice
    }
}




data class Camera(val model: String, val image: String, val price: String)
