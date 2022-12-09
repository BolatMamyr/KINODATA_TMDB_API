package com.example.kinodata.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.credit.person.personMovies.PersonMovies

class PersonMoviesVerticalAdapter
    : RecyclerView.Adapter<PersonMoviesVerticalAdapter.MyViewHolder>() {

        var onItemClick: ((PersonMovies?) -> Unit)? = null
        private var movies = emptyList<PersonMovies>()

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView
            val releaseDate: TextView
            val character: TextView
            val voteAve: TextView
            val voteCount: TextView
            val runtime: TextView
            val countries: TextView
            val img: ImageView

            init {
                title = itemView.findViewById(R.id.txt_title_vertical)
                releaseDate = itemView.findViewById(R.id.txt_releaseDate_vertical)
                character = itemView.findViewById(R.id.txt_character)
                voteAve = itemView.findViewById(R.id.txt_voteAve_vertical)
                voteCount = itemView.findViewById(R.id.txt_voteCount_vertical)
                runtime = itemView.findViewById(R.id.txt_runtime_vertical)
                countries = itemView.findViewById(R.id.txt_country_vertical)
                img = itemView.findViewById(R.id.img_vertical_list)
            }
        }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = movies[position].title
        holder.releaseDate.text = movies[position].release_date
        val character = movies[position].character
        holder.character.text = "<<$character>>"

        // Date
        val date = movies[position].release_date
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
        val vote = movies[position].vote_average
        holder.voteAve.text = String.format("%.1f", vote)
        holder.voteCount.text = movies[position].vote_count.toString()

        val img = movies[position].poster_path
        Glide.with(holder.itemView.context)
            .load(MyConstants.IMG_BASE_URL + img).into(holder.img)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movies[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater
                .from(parent.context).inflate(R.layout.item_vertical_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateData(newList: List<PersonMovies>) {
        val oldList = movies
        val diffResult = DiffUtil.calculateDiff(PersonMoviesDiffCallback(oldList, newList))
        movies = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class PersonMoviesDiffCallback(
        private val oldList: List<PersonMovies>,
        private val newList: List<PersonMovies>,
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