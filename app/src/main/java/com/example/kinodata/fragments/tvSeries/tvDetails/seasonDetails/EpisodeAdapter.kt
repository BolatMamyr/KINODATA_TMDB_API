package com.example.kinodata.fragments.tvSeries.tvDetails.seasonDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kinodata.databinding.ItemSeasonDetailsBinding
import com.example.kinodata.model.tv.season.Episode

class EpisodeAdapter(private val list: List<Episode>)
    : RecyclerView.Adapter<EpisodeAdapter.MyViewHolder>(){

    private var isExpanded = false
    inner class MyViewHolder(val binding: ItemSeasonDetailsBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSeasonDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            txtEpisodeNumber.text = (position + 1).toString()
            txtEpisodeName.text = list[position].name
            val runtime = if (list[position].runtime != 0) {
                "${list[position].runtime} min"
            } else ""
            txtEpisodeRuntime.text = runtime
            txtEpisodeOverview.text = list[position].overview
        }

        holder.itemView.setOnClickListener {
            if (!list[position].isExpanded) {
                list[position].isExpanded = true
                holder.binding.layoutExpandedEpisodeInfo.visibility = View.VISIBLE
                holder.binding.icArrowDown.visibility = View.GONE
            } else {
                list[position].isExpanded = false
                holder.binding.layoutExpandedEpisodeInfo.visibility = View.GONE
                holder.binding.icArrowDown.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = list.size
}