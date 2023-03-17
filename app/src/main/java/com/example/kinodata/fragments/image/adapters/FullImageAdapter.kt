package com.example.kinodata.fragments.image.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemFullImageBinding

class FullImageAdapter(
    private val list: List<String>
) : RecyclerView.Adapter<FullImageAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemFullImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFullImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 25f
        }
        circularProgressDrawable.start()
        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(holder.itemView.context)
            .load(MyConstants.IMG_BASE_URL + list[position])
            .placeholder(circularProgressDrawable)
            .apply(requestOptions)
            .into(holder.binding.imgFullImg)
    }

    override fun getItemCount(): Int = list.size
}