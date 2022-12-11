package com.example.kinodata.paging

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
import com.example.kinodata.model.tv.RTvSeries

class TvVerticalAdapter : PagingDataAdapter<RTvSeries, TvVerticalAdapter.MyViewHolder>(diffUtil) {

    var onItemClick: ((RTvSeries?) -> Unit)? = null

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
        val diffUtil = object : DiffUtil.ItemCallback<RTvSeries>() {
            override fun areItemsTheSame(oldItem: RTvSeries, newItem: RTvSeries): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RTvSeries, newItem: RTvSeries): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = getItem(position)?.name

        // Date
        val date = getItem(position)?.first_air_date
        if (date?.length!! > 8) {
            val year = date.substring(0, 4)
            var month = date.substring(5, 7)
            var day = date.substring(8)
            if (day.get(0) == '0') day = day[1].toString()

            month = when (month) {
                "01" -> holder.itemView.resources.getString(R.string.january)
                "02" -> holder.itemView.resources.getString(R.string.february)
                "03" -> holder.itemView.resources.getString(R.string.march)
                "04" -> holder.itemView.resources.getString(R.string.april)
                "05" -> holder.itemView.resources.getString(R.string.may)
                "06" -> holder.itemView.resources.getString(R.string.june)
                "07" -> holder.itemView.resources.getString(R.string.july)
                "08" -> holder.itemView.resources.getString(R.string.august)
                "09" -> holder.itemView.resources.getString(R.string.september)
                "10" -> holder.itemView.resources.getString(R.string.october)
                "11" -> holder.itemView.resources.getString(R.string.november)
                "12" -> holder.itemView.resources.getString(R.string.december)
                else -> ""
            }

            holder.releaseDate.text = "$day $month, $year"
        }
        holder.voteAve.text = getItem(position)?.vote_average.toString()
        holder.voteCount.text = getItem(position)?.vote_count.toString()

        // TODO: get genres, countries and runtime OR change layout item: Vote Ave to right top

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