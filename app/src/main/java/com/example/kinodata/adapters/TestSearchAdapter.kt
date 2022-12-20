package com.example.kinodata.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.kinodata.R

class TestSearchAdapter(private val mContext: Context, private val list: List<Item>) : BaseAdapter(), Filterable {

    private val mList = list
    private var mSuggestions = mutableListOf<Item>()


    override fun getCount(): Int = mSuggestions.size

    override fun getItem(p0: Int): Item = mSuggestions[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (p1 == null) {
            view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_search_suggestions, p2, false)
            holder = ViewHolder()
            holder.imageView = view.findViewById(R.id.img_searchSuggestion)
            holder.textView = view.findViewById(R.id.txt_searchSuggestion_title)
            view.tag = holder
        } else {
            view = p1
            holder = view.tag as ViewHolder
        }

        val item = getItem(p0)
        holder.imageView.setImageResource(item.resourceId)
        holder.textView.text = item.text

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                return FilterResults()
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                TODO("Not yet implemented")
            }
        }
    }

    private class ViewHolder {
        lateinit var imageView: ImageView
        lateinit var textView: TextView
    }

    private fun getSuggestions(text: String): List<Item> {
        return mList.filter { it.text.lowercase().contains(text) }
    }

    data class Item(val text: String, val resourceId: Int = R.drawable.cast)
}