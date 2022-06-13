package com.example.lapcak_99game.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lapcak_99game.R


class PictureAdapter(private val pictureList: ArrayList<Picture>) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {

    var onItemClick: ((Picture) -> Unit)? = null

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val imageView: ImageView = itemView.findViewById(R.id.iconCard)
        val textView: TextView = itemView.findViewById(R.id.textCard)
        val percProgress: ProgressBar = itemView.findViewById(R.id.progressBCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.level_item, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = pictureList[position]
        holder.imageView.setImageResource(picture.image)
        holder.textView.text = picture.name
        holder.percProgress.progress = picture.percentage

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(picture)
        }
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }
}