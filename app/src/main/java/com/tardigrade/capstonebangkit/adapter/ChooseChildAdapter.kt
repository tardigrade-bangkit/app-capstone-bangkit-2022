package com.tardigrade.capstonebangkit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.tardigrade.capstonebangkit.data.model.ChildProfile
import com.tardigrade.capstonebangkit.databinding.ChooseChildItemBinding
import com.tardigrade.capstonebangkit.utils.loadImage

class ChooseChildAdapter(
    mContext: Context,
    private val listChild: List<ChildProfile>
) : ArrayAdapter<ChildProfile>(mContext, 0, listChild) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ChooseChildItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            ChooseChildItemBinding.bind(convertView)
        }

        val (_, avatarUrl, name) = listChild[position]

        binding.apply {
            childAvatar.loadImage(avatarUrl)

            childAvatar.contentDescription = name
            childName.text = name
        }

        return binding.root
    }

    override fun getFilter(): Filter {
        return ChildFilter()
    }

    inner class ChildFilter(
    ) : Filter() {
        override fun performFiltering(constraints: CharSequence?): FilterResults {
            return FilterResults()
        }

        override fun publishResults(constraints: CharSequence?, results: FilterResults?) {
            //
        }
    }
}