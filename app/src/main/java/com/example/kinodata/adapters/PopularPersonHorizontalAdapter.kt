package com.example.kinodata.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemHorizontalPopularPersonsBinding
import com.example.kinodata.model.persons.popular.PopularPerson

class PopularPersonHorizontalAdapter
    : RecyclerView.Adapter<PopularPersonHorizontalAdapter.MyViewHolder>() {

    private var list = emptyList<PopularPerson>()
    var onItemClick: ((PopularPerson?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemHorizontalPopularPersonsBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHorizontalPopularPersonsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            txtHorizontalPopularPersonName.text = list[position].name
            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + list[position].profile_path)
                .into(imgHorizontalPopularPersons)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }

    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<PopularPerson>) {
        val oldList = list
        val diffResult = DiffUtil.calculateDiff(MyDiffCallBack(oldList, newList))
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class MyDiffCallBack(
        private val oldList: List<PopularPerson>,
        private val newList: List<PopularPerson>
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