package com.tardigrade.capstonebangkit.misc

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecoration(val height: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = height

        if (parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount?.minus(1) ?: 0)) {
            outRect.bottom = height;
        }
    }
}