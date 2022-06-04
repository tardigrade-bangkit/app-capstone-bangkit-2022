package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.databinding.AddChildProfileBigBinding
import com.tardigrade.capstonebangkit.databinding.ChildProfileBigBinding
import com.tardigrade.capstonebangkit.utils.loadImage

class ChildProfileBigAdapter(private val listChild: ArrayList<ChildProfile>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(child: ChildProfile)
        fun onFooterClicked()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ChildViewHolder(val binding: ChildProfileBigBinding)
        : RecyclerView.ViewHolder(binding.root)
    inner class FooterViewHolder(val binding: AddChildProfileBigBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FOOTER_TYPE) {
            val binding = AddChildProfileBigBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterViewHolder(binding)
        }

        val binding = ChildProfileBigBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChildViewHolder) {
            val (avatarUrl, name, level, tookTest) = listChild[position]

            holder.binding.apply {
                childAvatar.loadImage(avatarUrl)

                childAvatar.contentDescription = name
                childName.text = name
                childLevel.text = if (tookTest) {
                    holder.itemView.context.getString(R.string.child_level, level)
                } else {
                    holder.itemView.context.getString(R.string.not_taken_test)
                }
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