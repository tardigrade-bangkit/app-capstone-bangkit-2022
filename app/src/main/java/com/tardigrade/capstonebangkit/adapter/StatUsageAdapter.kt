package com.tardigrade.capstonebangkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tardigrade.capstonebangkit.data.model.DailyUsage
import com.tardigrade.capstonebangkit.databinding.WeeklyUsagesItemBinding
import com.tardigrade.capstonebangkit.utils.format
import java.text.SimpleDateFormat
import java.util.*

class StatUsageAdapter(private val usages: ArrayList<DailyUsage>) :
    RecyclerView.Adapter<StatUsageAdapter.UsageViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(dailyUsage: DailyUsage)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class UsageViewHolder(val binding: WeeklyUsagesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val binding = WeeklyUsagesItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UsageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        val dailyUsage = usages[position]

        val hours = dailyUsage.duration.toFloat() / (60 * 60 * 1000)

        holder.binding.date.text =
            SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault()).format(dailyUsage.date)
        holder.binding.duration.text = "${hours.format(2)} Jam"

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(usages[holder.adapterPosition])
        }
    }

    override fun getItemCount() = usages.size
}