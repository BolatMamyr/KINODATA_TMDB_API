package com.example.kinodata.fragments.image.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemFullImageBinding

class FullImageAdapter(
    private val list: List<String>
) : RecyclerView.Adapter<FullImageAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemFullImageBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFullImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imgFullImg.apply {
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_FULL_BASE_URL + list[position])
                .into(this)
        }
    }

    override fun getItemCount(): Int = list.size

}