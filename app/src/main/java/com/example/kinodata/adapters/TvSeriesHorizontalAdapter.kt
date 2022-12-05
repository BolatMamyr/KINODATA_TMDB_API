package com.example.kinodata.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.tv.RTvSeries

class TvSeriesHorizontalAdapter : RecyclerView.Adapter<TvSeriesHorizontalAdapter.MyViewHolder>() {

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
        var title = tvSeries[position].name
        tvSeriesId = tvSeries[position].id
        if (title.length > 19) {
            title = title.substring(0, 19) + "..."
        }
        holder.title.text = title
        // Date
        val date = tvSeries[position].first_air_date
        if(date.length > 7) {
            val year = date.substring(0, 4)
            var month = date.substring(5, 7)
            var day = date.substring(8)
            if (day[0] == '0') day = day[1].toString()

            month = when(month) {
                "01" -> getStringResource(holder, R.string.january)
                "02" -> getStringResource(holder, R.string.february)
                "03" -> getStringResource(holder, R.string.march)
                "04" -> getStringResource(holder, R.string.april)
                "05" -> getStringResource(holder, R.string.may)
                "06" -> getStringResource(holder, R.string.june)
                "07" -> getStringResource(holder, R.string.july)
                "08" -> getStringResource(holder, R.string.august)
                "09" -> getStringResource(holder, R.string.september)
                "10" -> getStringResource(holder, R.string.october)
                "11" -> getStringResource(holder, R.string.november)
                "12" -> getStringResource(holder, R.string.december)
                else -> ""
            }
            holder.date.text = "$day $month, $year"
        }


        val vote = tvSeries[position].vote_average
        holder.vote.text = vote.toString()

        if (vote == .0) {
            holder.card_vote.visibility = View.GONE
        } else {
            val colorId = if (vote < 5.0) {
                holder.itemView.resources.getColor(R.color.red, null)
            } else if (vote < 7.0) {
                holder.itemView.resources.getColor(R.color.gray, null)
            } else  {
                holder.itemView.resources.getColor(R.color.green, null)
            }
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