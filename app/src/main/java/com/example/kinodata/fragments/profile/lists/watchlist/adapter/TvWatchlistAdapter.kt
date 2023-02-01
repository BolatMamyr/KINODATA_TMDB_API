package com.example.kinodata.fragments.profile.lists.watchlist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemVerticalListBinding
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.utils.MyUtils

class TvWatchlistAdapter
    : PagingDataAdapter<RTvSeries, TvWatchlistAdapter.MyViewHolder>(diffUtil) {

    var onItemClick: ((RTvSeries?) -> Unit)? = null

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RTvSeries>() {
            override fun areItemsTheSame(oldItem: RTvSeries, newItem: RTvSeries): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RTvSeries, newItem: RTvSeries): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(val binding: ItemVerticalListBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            txtTitleVertical.text = getItem(position)?.name
            // Date
            val date = getItem(position)?.first_air_date
            txtReleaseDateVertical.text = date?.let {
                MyUtils.getFormattedDate(
                    it,
                    holder.itemView
                )
            }

            // Rating
            val rating = getItem(position)?.vote_average

            txtVoteAveVertical.text = String.format("%.1f", rating)
            txtVoteCountVertical.text = getItem(position)?.vote_count.toString()

            val colorId = rating?.let { MyUtils.getRatingColorId(it, holder.itemView) }
            colorId?.let { txtVoteAveVertical.setTextColor(it) }

            val img = getItem(position)?.poster_path
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + img).into(imgVerticalList)

            // setting onClickListener
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(getItem(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemVerticalListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}