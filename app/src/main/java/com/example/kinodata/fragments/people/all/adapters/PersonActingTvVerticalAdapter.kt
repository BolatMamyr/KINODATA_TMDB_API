package com.example.kinodata.fragments.people.all.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.persons.person.personTvSeries.PersonActingTv
import com.example.kinodata.utils.MyUtils

class PersonActingTvVerticalAdapter
    : RecyclerView.Adapter<PersonActingTvVerticalAdapter.MyViewHolder>() {

    var onItemClick: ((PersonActingTv?) -> Unit)? = null
    private var tvSeries = emptyList<PersonActingTv>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val releaseDate: TextView
        val character: TextView
        val voteAve: TextView
        val voteCount: TextView

        val img: ImageView

        init {
            title = itemView.findViewById(R.id.txt_title_vertical)
            releaseDate = itemView.findViewById(R.id.txt_releaseDate_vertical)
            character = itemView.findViewById(R.id.txt_character)
            voteAve = itemView.findViewById(R.id.txt_voteAve_vertical)
            voteCount = itemView.findViewById(R.id.txt_voteCount_vertical)
            img = itemView.findViewById(R.id.img_vertical_list)
        }
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = tvSeries[position].name
        holder.releaseDate.text = tvSeries[position].first_air_date
        val character = tvSeries[position].character
        holder.character.text = character

        // Date
        val date = tvSeries[position].first_air_date
        holder.releaseDate.text = MyUtils.getFormattedDate(date, holder.itemView)

        val vote = tvSeries[position].vote_average
        holder.voteAve.text = String.format("%.1f", vote)
        holder.voteCount.text = tvSeries[position].vote_count.toString()

        val colorId = MyUtils.getRatingColorId(vote, holder.itemView)
        holder.voteAve.setTextColor(colorId)

        val img = tvSeries[position].poster_path
        Glide.with(holder.itemView.context)
            .load(MyConstants.IMG_BASE_URL + img).into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tvSeries[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater
                .from(parent.context).inflate(R.layout.item_vertical_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return tvSeries.size
    }

    fun updateData(newList: List<PersonActingTv>) {
        val oldList = tvSeries
        val diffResult = DiffUtil.calculateDiff(PersonTvSeriesDiffCallback(oldList, newList))
        tvSeries = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class PersonTvSeriesDiffCallback(
        private val oldList: List<PersonActingTv>,
        private val newList: List<PersonActingTv>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}