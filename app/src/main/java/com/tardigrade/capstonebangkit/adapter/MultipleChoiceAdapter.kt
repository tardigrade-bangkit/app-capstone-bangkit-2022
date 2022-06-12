package com.tardigrade.capstonebangkit.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tardigrade.capstonebangkit.customviews.AnswerCard
import com.tardigrade.capstonebangkit.data.model.Choice

class MultipleChoiceAdapter(private val listChoice: ArrayList<Choice>) :
    RecyclerView.Adapter<MultipleChoiceAdapter.ViewHolder>() {
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: AnswerCard

        init {
            view = v as AnswerCard
        }
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    private var selectedPosition: Int = -1

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = AnswerCard(parent.context)
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (choiceName, choiceImage, choiceAudio, choiceText) = listChoice[position]
        holder.view.apply {
            isSelected = (position == selectedPosition)
            setAnswerContent(choiceName, choiceText, choiceImage, choiceAudio)
            holder.view.setOnClickListener {
                onItemClickCallback?.onItemClicked(listChoice[position], holder.view)
                selectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun getSelectedItem() = listChoice[selectedPosition]

    interface OnItemClickCallback {
        fun onItemClicked(data: Choice, view: AnswerCard)
    }

    override fun getItemCount() = listChoice.size
}