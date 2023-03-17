package com.example.kinodata.adapters.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.databinding.ItemVideoListBinding
import com.example.kinodata.model.videos.MyVideo
import com.example.kinodata.utils.MyUtils

class VideoListAdapter(private val list: List<MyVideo>) : RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {

    var onItemClick: ((MyVideo?) -> Unit)? = null
    inner class MyViewHolder(val binding: ItemVideoListBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemVideoListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            val title = MyUtils.getShortenedString25(list[position].name)
            txtVideoTitle.text = title
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

}