package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tardigrade.capstonebangkit.data.model.Achievement
import com.tardigrade.capstonebangkit.data.model.Badge
import com.tardigrade.capstonebangkit.databinding.LatestAchievementsItemBinding
import com.tardigrade.capstonebangkit.databinding.LatestBadgesItemBinding
import com.tardigrade.capstonebangkit.utils.loadImage

class StatAchievementAdapter(private val achievements: ArrayList<Achievement>) :
    RecyclerView.Adapter<StatAchievementAdapter.AchievementViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(achievement: Achievement)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class AchievementViewHolder(val binding: LatestAchievementsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = LatestAchievementsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]

        holder.binding.achievementName.text = achievement.name

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(achievements[holder.adapterPosition])
        }
    }

    override fun getItemCount() = achievements.size
}