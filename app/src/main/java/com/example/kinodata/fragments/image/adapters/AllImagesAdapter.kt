package com.example.kinodata.fragments.image.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemAllImagesBinding

class AllImagesAdapter : RecyclerView.Adapter<AllImagesAdapter.MyViewHolder>() {

    private var images = emptyList<String>()
    var onItemClick: ((Int?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemAllImagesBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAllImagesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imgAllImages.apply {
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
        val diffResult = DiffUtil.calculateDiff(AllImagesDiffCallback(oldList, newList))
        images = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class AllImagesDiffCallback(
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