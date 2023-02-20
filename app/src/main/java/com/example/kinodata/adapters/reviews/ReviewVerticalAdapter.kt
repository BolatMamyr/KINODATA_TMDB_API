package com.example.kinodata.adapters.reviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kinodata.R
import com.example.kinodata.constants.MyConstants
import com.example.kinodata.model.review.Review
import de.hdodenhof.circleimageview.CircleImageView

class ReviewVerticalAdapter : RecyclerView.Adapter<ReviewVerticalAdapter.MyViewHolder>(){

    private var reviews = emptyList<Review>()
    var onItemClick: ((Review?) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val line: View
        val img: CircleImageView
        val txtProfile: TextView
        val txtDate: TextView
        val txtReview: TextView
        val txtRating: TextView
        val layoutRating: RelativeLayout

        init {
            line = itemView.findViewById(R.id.line_allReviews)
            img = itemView.findViewById(R.id.img_allReviews_author)
            txtProfile = itemView.findViewById(R.id.txt_allReviews_author)
            txtDate = itemView.findViewById(R.id.txt_allReviews_date)
            txtReview = itemView.findViewById(R.id.txt_allReviews_content)
            layoutRating = itemView.findViewById(R.id.layout_allReviews_rating)
            txtRating = itemView.findViewById(R.id.txt_allReviews_rating)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtProfile.text = reviews[position].author_details.username
        holder.txtDate.text = reviews[position].getDate(holder.itemView.context)
        holder.txtReview.text = reviews[position].content.take(180).plus("...")

        val rating = reviews[position].author_details.rating

        val colorId = if (rating != .0) {
            if (rating < 5.0) {
                holder.itemView.resources.getColor(R.color.red, null)
            } else if (rating < 7.0) {
                holder.itemView.resources.getColor(R.color.gray, null)
            } else {
                holder.itemView.resources.getColor(R.color.green, null)
            }
        } else {
            holder.itemView.resources.getColor(R.color.super_light_gray, null)
        }

        if (rating != .0) {
            holder.txtRating.text = rating.toString()
        } else {
            holder.txtRating.text = ""
        }
        holder.line.setBackgroundColor(colorId)
        holder.layoutRating.setBackgroundColor(colorId)
        val avatarPath = reviews[position].author_details.avatar_path
        if (avatarPath != null) {
            if (avatarPath.contains("http")) {
                val index = avatarPath.indexOf("http")
                Glide.with(holder.itemView.context)
                    .load(avatarPath.substring(index))
                    .into(holder.img)
            } else {
                Glide.with(holder.itemView.context)
                    .load(MyConstants.IMG_BASE_URL + avatarPath)
                    .into(holder.img)
            }
        } else {
            holder.img.setImageResource(R.drawable.avatar)
        }

        holder.itemView.setOnClickListener { onItemClick?.invoke(reviews[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vertical_reviews_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun updateData(newList: List<Review>) {
        val oldList = reviews
        val diffResult = DiffUtil.calculateDiff(ReviewDiffCallback(oldList, newList))
        reviews = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ReviewDiffCallback(
        private val oldList: List<Review>,
        private val newList: List<Review>
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