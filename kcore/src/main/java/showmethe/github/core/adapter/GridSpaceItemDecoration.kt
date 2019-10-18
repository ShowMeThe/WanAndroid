package showmethe.github.core.adapter

import android.graphics.Rect
import android.view.View

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */
class GridSpaceItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % this.spanCount
        if (this.includeEdge) {
            outRect.left = this.spacing - column * this.spacing / this.spanCount
            outRect.right = (column + 1) * this.spacing / this.spanCount
            if (position < this.spanCount) {
                outRect.top = this.spacing
            }

            outRect.bottom = this.spacing
        } else {
            outRect.left = column * this.spacing / this.spanCount
            outRect.right = this.spacing - (column + 1) * this.spacing / this.spanCount
            if (position < this.spanCount) {
                outRect.top = this.spacing
            }

            outRect.bottom = this.spacing
        }

    }
}
