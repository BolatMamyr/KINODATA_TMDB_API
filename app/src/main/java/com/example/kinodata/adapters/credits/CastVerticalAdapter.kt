package com.example.kinodata.adapters.credits

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
import com.example.kinodata.model.persons.media_credits.Cast

class CastVerticalAdapter : RecyclerView.Adapter<CastVerticalAdapter.MyViewHolder>() {

    private var cast = emptyList<Cast>()
    var onItemClick: ((Cast?) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView
        val txt_name: TextView
        val txt_characterName: TextView

        init {
            img = itemView.findViewById(R.id.img_vertical_cast)
            txt_name = itemView.findViewById(R.id.txt_verticalCast_name)
            txt_characterName = itemView.findViewById(R.id.txt_verticalCast_characterName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vertical_cast, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_name.text = cast[position].name
        holder.txt_characterName.text = cast[position].character

        if (cast[position].profile_path == null) {
            holder.img.setImageResource(R.drawable.profileblankpic)
            holder.img.scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + cast[position].profile_path)
                .into(holder.img)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cast[position])
        }
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    fun updateData(newList: List<Cast>) {
        val oldList = cast
        val diffResult = DiffUtil.calculateDiff(CastDiffCallback(oldList, newList))
        cast = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class CastDiffCallback(
        var oldList: List<Cast>,
        var newList: List<Cast>
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