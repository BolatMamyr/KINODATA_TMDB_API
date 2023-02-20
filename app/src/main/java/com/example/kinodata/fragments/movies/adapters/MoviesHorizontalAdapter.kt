package com.example.kinodata.fragments.movies.adapters

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
import com.example.kinodata.model.movie.RMovie
import com.example.kinodata.utils.MyUtils

class MoviesHorizontalAdapter : RecyclerView.Adapter<MoviesHorizontalAdapter.MyViewHolder>() {

    private var movies = emptyList<RMovie>()

    var onItemClick : ((RMovie?) -> Unit)? = null
    var movieId: Int? = null

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
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horizontal_list, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val title = movies[position].title
        movieId = movies[position].id

        holder.title.text = MyUtils.getShortenedString(title)
        // Date
        val date = movies[position].release_date
        holder.date.text = MyUtils.getFormattedDate(date, holder.itemView)

        val vote = movies[position].vote_average
        holder.vote.text = String.format("%.1f", vote)

        if (vote == .0) {
            holder.card_vote.visibility = View.GONE
        } else {
            val colorId = MyUtils.getRatingColorId(vote, holder.itemView)
            holder.rl_vote.setBackgroundColor(colorId)
        }


        val img = movies[position].poster_path
        Glide.with(holder.itemView.context).load(MyConstants.IMG_BASE_URL + img).into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movies[position])
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateData(newList: List<RMovie>) {
        val oldList = movies
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ResultMovieDiffCallback(oldList, newList)
        )
        movies = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class ResultMovieDiffCallback(
        var oldList: List<RMovie>,
        var newList: List<RMovie>
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