package com.example.kinodata.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.databinding.ItemVerticalListBinding
import com.example.kinodata.model.persons.popular.PopularPerson

class PopularPersonsPagingAdapter
    : PagingDataAdapter<PopularPerson, PopularPersonsPagingAdapter.MyViewHolder>(diffCallback) {

    var onItemClick: ((PopularPerson?) -> Unit)? = null

    inner class MyViewHolder(val binding: ItemVerticalListBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            txtTitleVertical.text = item?.name

            Glide.with(holder.itemView.context)
                .load(MyConstants.IMG_BASE_URL + item?.profile_path)
                .into(imgVerticalList)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemVerticalListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PopularPerson>() {
            override fun areItemsTheSame(oldItem: PopularPerson, newItem: PopularPerson): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PopularPerson, newItem: PopularPerson): Boolean {
                return oldItem == newItem
            }
        }
    }
}