package com.tardigrade.capstonebangkit.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Log.INFO
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tardigrade.capstonebangkit.customviews.LessonCard
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.data.model.Lesson
import com.tardigrade.capstonebangkit.databinding.AddChildButtonBinding
import com.tardigrade.capstonebangkit.databinding.ChildProfileBinding

class LessonAdapter(private val listLesson: ArrayList<Lesson>)
    : RecyclerView.Adapter<LessonAdapter.ViewHolder>(){
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val view: LessonCard
        init {
            view = v as LessonCard
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LessonCard(parent.context)
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (coverImageUrl, title, type) = listLesson[position]
        holder.view.apply {
            setLessonTitle(title)
            setLessonType(type)
            Glide.with(context)
                .asBitmap()
                .load(coverImageUrl)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        setLessonCoverImage(resource)
                    }
                })
            setOnClickListener { onItemClickCallback.onItemClicked(listLesson[position], holder.view) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Lesson?, view: LessonCard)
    }

    override fun getItemCount() = listLesson.size
}