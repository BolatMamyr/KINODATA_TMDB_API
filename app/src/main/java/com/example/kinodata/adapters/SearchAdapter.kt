package com.example.kinodata.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.multiSearch.SearchResult

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
            holder.txt = view.findViewById(R.id.txt_searchSuggestion_title)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as MyViewHolder
        }

        val item = getItem(position)

        if (item?.media_type == "person") {
            holder.txt.text = item.name
            Glide.with(mContext)
                .load(MyConstants.IMG_BASE_URL + item.profile_path)
                .into(holder.img)
        } else {
            Glide.with(mContext)
                .load(MyConstants.IMG_BASE_URL + item?.poster_path)
                .into(holder.img)
            if (item?.media_type == "movie") {
                holder.txt.text = item.title
            } else {
                // else TV
                holder.txt.text = item?.name
            }
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
                it.title.lowercase().contains(text.lowercase())
            } else {
                it.name.lowercase().contains(text.lowercase())
            }
        }
    }

    fun updateData(newList: List<SearchResult>) {
        mList = newList
        notifyDataSetChanged()
    }

    private class MyViewHolder {
        lateinit var txt: TextView
        lateinit var img: ImageView
    }
}