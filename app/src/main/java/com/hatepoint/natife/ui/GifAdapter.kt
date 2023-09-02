package com.hatepoint.natife.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hatepoint.natife.R
import com.hatepoint.natife.databinding.ItemGifBinding

class GifAdapter: RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    var onItemClick: ((position: Int) -> Unit)? = null

    var gifs = emptyList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val binding = ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = gifs[position]
        Glide.with(holder.itemView.context)
            .asGif()
            .load(gif)
            .apply(RequestOptions().override(500,500).placeholder(R.drawable.baseline_gif_24).error(R.drawable.baseline_broken_image_24))
            .into(holder.gifImageView)
        holder.itemView.setOnClickListener { onItemClick?.invoke(position) }
    }

    override fun getItemCount(): Int {
        return gifs.size
    }

    class GifViewHolder(val binding: ItemGifBinding): RecyclerView.ViewHolder(binding.root) {
        val gifImageView: ImageView = itemView.findViewById(R.id.gifImageView)
    }
}