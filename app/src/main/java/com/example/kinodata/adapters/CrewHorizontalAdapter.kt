package com.example.kinodata.adapters

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
import com.example.kinodata.model.persons.media_credits.Crew

class CrewHorizontalAdapter : RecyclerView.Adapter<CrewHorizontalAdapter.MyViewHolder>() {

    private var crewList = emptyList<Crew>()
    var onItemClick: ((Crew?) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val job: TextView
        val img: ImageView

        init {
            name = itemView.findViewById(R.id.txt_crew_name)
            job = itemView.findViewById(R.id.txt_crew_job)
            img = itemView.findViewById(R.id.img_crew)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_horizontal_crew, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = crewList[position].name
        holder.job.text = crewList[position].job

        val picture = crewList[position].profile_path
        if (picture == null) {
            holder.img.setImageResource(R.drawable.profileblankpic)
        } else {
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + crewList[position].profile_path)
                .into(holder.img)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(crewList[position])
        }
    }

    override fun getItemCount(): Int {
        return crewList.size
    }

    fun updateData(newList: List<Crew>) {
        val oldList = crewList
        val diffResult = DiffUtil.calculateDiff(CrewDiffCallback(oldList, newList))
        crewList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class CrewDiffCallback(
        private val oldList: List<Crew>,
        private val newList: List<Crew>
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