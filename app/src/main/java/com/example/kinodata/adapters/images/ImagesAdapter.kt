package com.example.kinodata.adapters.images

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemImagesBinding
import com.example.kinodata.model.images.Backdrop

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {

    private var images = emptyList<Backdrop>()
    var onItemClick: ((Backdrop?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemImagesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imgImages.apply {
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + images[position].file_path)
                .into(this)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(images[position])
        }
    }

    override fun getItemCount(): Int = images.size

    fun updateData(newList: List<Backdrop>) {
        val oldList = images
        val diffResult = DiffUtil.calculateDiff(
            ImagesDiffCallback(
                oldList,
                newList
            )
        )
        images = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ImagesDiffCallback(
        private val oldList: List<Backdrop>,
        private val newList: List<Backdrop>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].file_path == newList[newItemPosition].file_path
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}