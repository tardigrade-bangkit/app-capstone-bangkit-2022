package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.databinding.AddChildButtonBinding
import com.tardigrade.capstonebangkit.databinding.ChildProfileBinding

class ChildProfileAdapter(private val listChild: ArrayList<ChildProfile>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    inner class ChildViewHolder(val binding: ChildProfileBinding)
        : RecyclerView.ViewHolder(binding.root)
    inner class FooterViewHolder(val binding: AddChildButtonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FOOTER_TYPE) {
            val binding = AddChildButtonBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return FooterViewHolder(binding)
        }

        val binding = ChildProfileBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChildViewHolder) {
            val (avatarUrl, name) = listChild[position]

            holder.binding.apply {
                Glide.with(holder.itemView.context)
                    .load(avatarUrl)
                    .into(childAvatar)

                childAvatar.contentDescription = name
                childName.text = name
            }
        } else if (holder is FooterViewHolder) {
            holder.binding.apply {
                addChildButton.setOnClickListener {
                    Toast.makeText(holder.itemView.context, "Touch Add", Toast.LENGTH_SHORT).show()
                }
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