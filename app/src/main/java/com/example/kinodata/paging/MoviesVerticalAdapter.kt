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
import com.example.kinodata.model.movie.RMovie

class MoviesVerticalAdapter : PagingDataAdapter<RMovie, MoviesVerticalAdapter.MyViewHolder>(
    diffUtil
) {

    var onItemClick: ((RMovie?) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val releaseDate: TextView
//        val genres: TextView
        val voteAve: TextView
        val voteCount: TextView
        val img: ImageView

        init {
            title = itemView.findViewById(R.id.txt_title_vertical)
            releaseDate = itemView.findViewById(R.id.txt_releaseDate_vertical)
//            genres = itemView.findViewById(R.id.txt_character)
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
//        holder.releaseDate.text = getItem(position)?.release_date

        // Date
        val date = getItem(position)?.release_date
        if (date?.length!! > 8) {
            val year = date.substring(0, 4)
            var month = date.substring(5, 7)
            var day = date.substring(8)
            if (day.get(0) == '0') day = day[1].toString()

            month = when (month) {
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

    private fun getStringResource(holder: MyViewHolder, resourceId: Int) : String {
        return holder.itemView.resources.getString(resourceId)
    }

}