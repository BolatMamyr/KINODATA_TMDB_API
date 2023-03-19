package com.example.kinodata.fragments.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemSearchSuggestionsBinding
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.utils.MyUtils

class SearchRvAdapter : RecyclerView.Adapter<SearchRvAdapter.MyViewHolder>() {

    private var list = emptyList<SearchResult>()
    var onItemClick: ((SearchResult?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemSearchSuggestionsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchSuggestionsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        val mContext = holder.itemView.context

        if (item.media_type == MyConstants.MEDIA_TYPE_PERSON) {
            holder.binding.txtSearchSuggestionTitle.text = item.name
            holder.binding.txtSearchSuggestionMediaType.text = mContext.getString(
                R.string.person
            )

            Glide.with(mContext)
                .load(MyConstants.IMG_BASE_URL + item.profile_path)
                .into(holder.binding.imgSearchSuggestion)
        } else {
            Glide.with(mContext)
                .load(MyConstants.IMG_BASE_URL + item.poster_path)
                .into(holder.binding.imgSearchSuggestion)

            // Rating
            val rating = item.vote_average
            holder.binding.txtSearchSuggestionRating.text = String.format("%.1f", rating)
            holder.binding.txtSearchSuggestionVoteCount.text = item.vote_count.toString()

            val colorId = MyUtils.getRatingColorId(rating, holder.itemView)
            holder.binding.txtSearchSuggestionRating.setTextColor(colorId)


            val date: String?
            if (item.media_type == MyConstants.MEDIA_TYPE_MOVIE) {
                holder.binding.txtSearchSuggestionTitle.text =
                    MyUtils.getShortenedString19(item.title)
                holder.binding.txtSearchSuggestionMediaType.text =
                    mContext.getString(R.string.movie)
                date = item.release_date
            } else {
                // else TV
                holder.binding.txtSearchSuggestionMediaType.text = mContext.getString(R.string.tv)
                holder.binding.txtSearchSuggestionTitle.text = item.name.let {
                    MyUtils.getShortenedString19(it)
                }
                date = item.first_air_date
            }
            holder.binding.txtSearchSuggestionReleaseDate.text =
                MyUtils.getFormattedDate(date, holder.itemView)

        }

        holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<SearchResult>) {
        val oldList = list
        val diffResult = DiffUtil.calculateDiff(MyDiffCallBack(oldList, newList))
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class MyDiffCallBack(
        private val oldList: List<SearchResult>,
        private val newList: List<SearchResult>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}