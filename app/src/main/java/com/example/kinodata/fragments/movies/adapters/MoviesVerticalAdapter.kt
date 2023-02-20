package com.example.kinodata.fragments.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.utils.MyUtils

class MoviesVerticalAdapter : PagingDataAdapter<RMovie, MoviesVerticalAdapter.MyViewHolder>(
    diffUtil
) {

    var onItemClick: ((RMovie?) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val releaseDate: TextView
        val voteAve: TextView
        val voteCount: TextView
        val img: ImageView

        init {
            title = itemView.findViewById(R.id.txt_title_vertical)
            releaseDate = itemView.findViewById(R.id.txt_releaseDate_vertical)
            voteAve = itemView.findViewById(R.id.txt_voteAve_vertical)
            voteCount = itemView.findViewById(R.id.txt_voteCount_vertical)
            img = itemView.findViewById(R.id.img_vertical_list)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RMovie>() {
            override fun areItemsTheSame(oldItem: RMovie, newItem: RMovie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RMovie, newItem: RMovie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = getItem(position)?.title

        // Date
        val date = getItem(position)?.release_date
        holder.releaseDate.text = date?.let { MyUtils.getFormattedDate(it, holder.itemView) }

        // Rating
        val rating = getItem(position)?.vote_average

        holder.voteAve.text = String.format("%.1f", rating)
        holder.voteCount.text = getItem(position)?.vote_count.toString()

        val colorId = rating?.let { MyUtils.getRatingColorId(it, holder.itemView) }
        colorId?.let { holder.voteAve.setTextColor(it) }

        val img = getItem(position)?.poster_path
        Glide.with(holder.itemView.context)
            .load(MyConstants.IMG_BASE_URL + img).into(holder.img)

        // setting onClickListener
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(getItem(position))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater
                .from(parent.context).inflate(R.layout.item_vertical_list, parent, false)
        )
    }



}