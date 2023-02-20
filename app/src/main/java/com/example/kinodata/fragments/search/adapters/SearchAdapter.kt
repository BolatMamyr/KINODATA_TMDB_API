package com.example.kinodata.fragments.search.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.multiSearch.SearchResult
import com.example.kinodata.utils.MyUtils

class SearchAdapter(
    private val mContext: Context,
    private val list: List<SearchResult>
) : BaseAdapter(), Filterable {

    private var mList = list
    private var mSuggestions = list
    private val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: MyViewHolder
        val view: View

        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_search_suggestions, parent, false)
            holder = MyViewHolder()
            holder.img = view.findViewById(R.id.img_searchSuggestion)
            holder.txt_title = view.findViewById(R.id.txt_searchSuggestion_title)
            holder.txt_date = view.findViewById(R.id.txt_searchSuggestion_releaseDate)
            holder.txt_rating = view.findViewById(R.id.txt_searchSuggestion_rating)
            holder.txt_voteCount = view.findViewById(R.id.txt_searchSuggestion_voteCount)
            holder.txt_mediaType = view.findViewById(R.id.txt_searchSuggestion_mediaType)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as MyViewHolder
        }

        val item = getItem(position)

        if (item?.media_type == MyConstants.MEDIA_TYPE_PERSON) {
            holder.txt_title.text = item.name
            holder.txt_mediaType.text = view.resources.getString(R.string.person)

            Glide.with(mContext)
                .load(MyConstants.IMG_BASE_URL + item.profile_path)
                .into(holder.img)
        } else {
            Glide.with(mContext)
                .load(MyConstants.IMG_BASE_URL + item?.poster_path)
                .into(holder.img)

            // Rating
            val rating = item?.vote_average
            holder.txt_rating.text = String.format("%.1f", rating)
            holder.txt_voteCount.text = item?.vote_count?.toString()

            if (rating != null) {
                val colorId = MyUtils.getRatingColorId(rating, view)
                holder.txt_rating.setTextColor(colorId)
            }

            val date: String?
            if (item?.media_type == MyConstants.MEDIA_TYPE_MOVIE) {
                holder.txt_title.text = MyUtils.getShortenedString(item.title)
                holder.txt_mediaType.text = view.resources.getString(R.string.movie)
                date = item.release_date
            } else {
                // else TV
                holder.txt_mediaType.text = view.resources.getString(R.string.tv)
                holder.txt_title.text = item?.name?.let { MyUtils.getShortenedString(it) }
                date = item?.first_air_date
            }

            holder.txt_date.text = date?.let { MyUtils.getFormattedDate(it, view) }
        }

        return view
    }

    override fun getCount(): Int {
        return mSuggestions.size
    }

    override fun getItem(position: Int): SearchResult? {
        return mSuggestions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter() = object : Filter() {

        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            if (p0 != null) {
                mSuggestions = getSuggestions(p0.toString())
                results.values = mSuggestions
                results.count = mSuggestions.size
            }
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            if (p1 != null && p1.count > 0) {
                notifyDataSetChanged()
            } else {
                mSuggestions = emptyList()
                notifyDataSetInvalidated()
            }
        }
    }

    private fun getSuggestions(text: String): List<SearchResult> {
        return mList.filter {
            if (it.media_type == "movie") {
                it.title.lowercase().contains(text.lowercase().trim())
            } else {
                // else tv or person
                it.name.lowercase().contains(text.lowercase().trim())
            }
        }
    }

    fun updateData(newList: List<SearchResult>) {
        mList = newList
        notifyDataSetChanged()
    }

    private class MyViewHolder {
        lateinit var txt_title: TextView
        lateinit var img: ImageView
        lateinit var txt_date: TextView
        lateinit var txt_rating: TextView
        lateinit var txt_voteCount: TextView
        lateinit var txt_mediaType: TextView
    }
}