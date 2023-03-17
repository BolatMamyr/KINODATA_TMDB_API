package com.example.kinodata.fragments.tvSeries.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemHorizontalSeasonsBinding
import com.example.kinodata.model.tv.tvDetails.Season

// TODO: Set list = seasons
class TvSeasonsAdapter(val list: List<Season>)
    : RecyclerView.Adapter<TvSeasonsAdapter.MyViewHolder>() {

    var onItemClick: ((Season?) -> Unit)? = null

    inner class MyViewHolder(val binding : ItemHorizontalSeasonsBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHorizontalSeasonsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            list[position].poster_path?.let { img ->
                Glide.with(holder.itemView.context)
                    .load(MyConstants.IMG_BASE_URL + img)
                    .into(imgHorizontalSeasons)
            }
            txtHorizontalSeasonNumber.text = list[position].name

            val episodeCount = list[position].episode_count
            val numberOfEpisodes = if (episodeCount > 1) {
                "$episodeCount ${holder.itemView.resources.getString(R.string.episodes)}"
            } else {
                "$episodeCount ${holder.itemView.resources.getString(R.string.episode)}"
            }
            txtHorizontalSeasonNumberOfEpisodes.text = numberOfEpisodes
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}