package com.example.kinodata.fragments.tvSeries.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.tv.RTvSeries
import com.example.kinodata.utils.MyUtils

class TvHorizontalAdapter : RecyclerView.Adapter<TvHorizontalAdapter.MyViewHolder>() {

    private var tvSeries = emptyList<RTvSeries>()
    var tvSeriesId: Int? = null
    var onItemClick: ((RTvSeries?) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView
        val title: TextView
        val date: TextView
        val vote: TextView
        val rl_vote: RelativeLayout
        val card_vote: CardView
        init {
            img = itemView.findViewById(R.id.img_horizontalList)
            title = itemView.findViewById(R.id.txt_horizontalList_title)
            date = itemView.findViewById(R.id.txt_horizontalList_date)
            vote = itemView.findViewById(R.id.txt_horizontalList_vote)
            rl_vote = itemView.findViewById(R.id.rl_vote)
            card_vote = itemView.findViewById(R.id.card_vote)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_horizontal_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val title = tvSeries[position].name
        tvSeriesId = tvSeries[position].id

        holder.title.text = MyUtils.getShortenedString19(title)
        // Date
        val date = tvSeries[position].first_air_date
        holder.date.text = MyUtils.getFormattedDate(date, holder.itemView)

        val vote = tvSeries[position].vote_average
        holder.vote.text = String.format("%.1f", vote)

        if (vote == .0) {
            holder.card_vote.visibility = View.GONE
        } else {
            val colorId = MyUtils.getRatingColorId(vote, holder.itemView)
            holder.rl_vote.setBackgroundColor(colorId)
        }

        val img = tvSeries[position].poster_path
        Glide.with(holder.itemView.context).load(MyConstants.IMG_BASE_URL + img)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tvSeries[position])
        }
    }

    override fun getItemCount(): Int {
        return tvSeries.size
    }

    fun updateData(newList: List<RTvSeries>) {
        val oldList = tvSeries
        val diffResult = DiffUtil.calculateDiff(
            TvSeriesCallback(oldList, newList)
        )
        tvSeries = newList
        diffResult.dispatchUpdatesTo(this)
    }
    private class TvSeriesCallback(
        private val oldList: List<RTvSeries>,
        private val newList: List<RTvSeries>
        ): DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private fun getStringResource(holder: MyViewHolder, resourceId: Int) : String {
        return holder.itemView.resources.getString(resourceId)
    }
}