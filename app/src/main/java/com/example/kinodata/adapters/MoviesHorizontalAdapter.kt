package com.example.kinodata.adapters

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
import com.example.kinodata.constants.MyConstants.Companion.IMG_BASE_URL
import com.example.kinodata.model.movie.RMovie

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
        var title = movies[position].title
        movieId = movies[position].id
        if (title.length > 19) {
            title = title.substring(0, 19) + "..."
        }
        holder.title.text = title
        // Date
        val date = movies[position].release_date
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


        val vote = movies[position].vote_average
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


        val img = movies[position].poster_path
        Glide.with(holder.itemView.context).load(IMG_BASE_URL + img).into(holder.img)

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

    private fun getStringResource(holder: MyViewHolder, resourceId: Int) : String {
        return holder.itemView.resources.getString(resourceId)
    }
}