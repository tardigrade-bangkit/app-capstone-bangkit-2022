package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tardigrade.capstonebangkit.databinding.AvatarItemBinding
import com.tardigrade.capstonebangkit.utils.loadImage

class AvatarAdapter(private val imageUrls: ArrayList<String>)
    : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(imageUrl: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class AvatarViewHolder(val binding: AvatarItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = AvatarItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val imageUrl = imageUrls[position]

        holder.binding.imageAvatarItem.loadImage(imageUrl)

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(imageUrl)
        }
    }

    override fun getItemCount() = imageUrls.size
}