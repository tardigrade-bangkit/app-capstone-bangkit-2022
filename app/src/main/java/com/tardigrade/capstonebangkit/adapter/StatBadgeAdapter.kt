package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tardigrade.capstonebangkit.data.model.Badge
import com.tardigrade.capstonebangkit.databinding.LatestBadgesItemBinding
import com.tardigrade.capstonebangkit.utils.loadImage

class StatBadgeAdapter(private val badges: ArrayList<Badge>) :
    RecyclerView.Adapter<StatBadgeAdapter.BadgeViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(badge: Badge)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class BadgeViewHolder(val binding: LatestBadgesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val binding = LatestBadgesItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BadgeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val (imageUrl) = badges[position]

        holder.binding.badgesImage.loadImage(imageUrl)

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(badges[holder.adapterPosition])
        }
    }

    override fun getItemCount() = badges.size
}