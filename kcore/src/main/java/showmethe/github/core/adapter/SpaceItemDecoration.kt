package showmethe.github.core.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */
class SpaceItemDecoration(//leftRight为横向间的距离 topBottom为纵向间距离
        private val leftRight: Int, private val topBottom: Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val layoutManager = (parent.layoutManager as androidx.recyclerview.widget.LinearLayoutManager?)!!
        //竖直方向的
        if (layoutManager.orientation == androidx.recyclerview.widget.LinearLayoutManager.VERTICAL) {
            //最后一项不需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.bottom = 0
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.right = leftRight
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.right = leftRight
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.bottom = topBottom
        }

    }

    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.onDraw(c, parent, state)

    }


}
