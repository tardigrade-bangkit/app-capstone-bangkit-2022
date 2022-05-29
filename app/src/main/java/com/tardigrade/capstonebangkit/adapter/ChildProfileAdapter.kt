package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.databinding.AddChildProfileSmallBinding
import com.tardigrade.capstonebangkit.databinding.ChildProfileSmallBinding
import com.tardigrade.capstonebangkit.misc.loadImage

class ChildProfileAdapter(private val listChild: ArrayList<ChildProfile>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(child: ChildProfile)
        fun onFooterClicked()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ChildViewHolder(val binding: ChildProfileSmallBinding)
        : RecyclerView.ViewHolder(binding.root)
    inner class FooterViewHolder(val binding: AddChildProfileSmallBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FOOTER_TYPE) {
            val binding = AddChildProfileSmallBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterViewHolder(binding)
        }

        val binding = ChildProfileSmallBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChildViewHolder) {
            val (avatarUrl, name) = listChild[position]

            holder.binding.apply {
                childAvatar.loadImage(avatarUrl)

                childAvatar.contentDescription = name
                childName.text = name
            }

            holder.itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(listChild[holder.adapterPosition])
            }
        } else if (holder is FooterViewHolder) {
            holder.itemView.setOnClickListener {
                onItemClickCallback?.onFooterClicked()
            }
        }
    }

    override fun getItemCount() = listChild.size + 1

    override fun getItemViewType(position: Int): Int {
        if (position == listChild.size) {
            return FOOTER_TYPE
        }

        return super.getItemViewType(position)
    }

    companion object {
        private const val FOOTER_TYPE = 1
    }
}