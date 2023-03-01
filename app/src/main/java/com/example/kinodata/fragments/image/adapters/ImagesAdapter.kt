package com.example.kinodata.fragments.image.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemImagesBinding

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {

    private var images = emptyList<String>()
    var onItemClick: ((Int?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemImagesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imgImages.apply {
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + images[position])
                .into(this)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int = images.size

    fun updateData(newList: List<String>) {
        val oldList = images
        val diffResult = DiffUtil.calculateDiff(ImagesDiffCallback(oldList, newList))
        images = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ImagesDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].hashCode() == newList[newItemPosition].hashCode()
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}