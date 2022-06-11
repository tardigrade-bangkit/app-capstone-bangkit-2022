package com.tardigrade.capstonebangkit.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.R
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.databinding.StatLessonsItemBinding

class StatLessonAdapter(private val lessons: ArrayList<Lesson>) :
    RecyclerView.Adapter<StatLessonAdapter.LessonViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(lesson: Lesson)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class LessonViewHolder(val binding: StatLessonsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = StatLessonsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]

        holder.binding.currentLessonName.text = lesson.title
        Glide.with(holder.itemView.context).asBitmap()
            .load(lesson.coverImageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val palette = Palette.from(resource).generate()
                    holder.binding.root.background.setTint(
                        palette.getLightVibrantColor(
                            palette.getLightMutedColor(
                                ContextCompat.getColor(
                                    holder.itemView.context,
                                    R.color.blue_100
                                )
                            )
                        )
                    )
                    holder.binding.currentLessonName.setTextColor(
                        palette.getDarkMutedColor(
                            palette.getDarkVibrantColor(
                                ContextCompat.getColor(
                                    holder.itemView.context,
                                    R.color.blue_800
                                )
                            )
                        )
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    //
                }

            })

        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(lessons[holder.adapterPosition])
        }
    }

    override fun getItemCount() = lessons.size
}