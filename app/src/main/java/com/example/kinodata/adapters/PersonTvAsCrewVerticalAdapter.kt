package com.example.kinodata.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemVerticalListBinding
import com.example.kinodata.model.persons.person.personTvSeries.PersonTvAsCrew
import com.example.kinodata.utils.MyUtils

class PersonTvAsCrewVerticalAdapter
    : RecyclerView.Adapter<PersonTvAsCrewVerticalAdapter.MyViewHolder>() {

    private var list: List<PersonTvAsCrew> = emptyList()
    var onItemClick: ((PersonTvAsCrew?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemVerticalListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemVerticalListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            txtTitleVertical.text = item.name
            txtReleaseDateVertical.text =
                MyUtils.getFormattedDate(item.first_air_date, holder.itemView)

            // Rating
            val rating = item.vote_average

            txtVoteAveVertical.text = String.format("%.1f", rating)
            txtVoteCountVertical.text = item.vote_count.toString()

            val colorId = MyUtils.getRatingColorId(rating, holder.itemView)
            txtVoteAveVertical.setTextColor(colorId)

            txtCharacter.text = item.job

            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + item.poster_path)
                .into(imgVerticalList)

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size


    private class MyDiffCallBack(
        private val oldList: List<PersonTvAsCrew>,
        private val newList: List<PersonTvAsCrew>
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

    fun updateData(newList: List<PersonTvAsCrew>) {
        val oldList = list
        val diffResult = DiffUtil.calculateDiff(
            MyDiffCallBack(oldList, newList)
        )
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

}