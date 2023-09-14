package com.hatepoint.natife.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hatepoint.natife.R
import com.hatepoint.natife.databinding.ActivityGifBinding

class GifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGifBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showGif()
    }

    private fun showGif() {
        Glide.with(this)
            .asGif()
            .load(intent.getStringExtra("gif"))
            .apply(
                RequestOptions().placeholder(R.drawable.baseline_gif_24).error(
                    R.drawable.baseline_broken_image_24
                )
            )
            .into(binding.gifImageView)
    }
}