package showmethe.github.core.adapter.slideAdapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import showmethe.github.core.R

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter.slideAdapter
 */
class SlideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews: SparseArray<View> = SparseArray()

    fun <T : View> getView(viewId: Int): T? {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }


    fun setText(text: String) {
        val textView = getView<TextView>(R.id.slideLayout_tv_content)
        if (textView != null) {
            textView.text = text
        }
    }

    fun setTextColor(context: Context, textColor: Int) {
        val textView = getView<TextView>(R.id.slideLayout_tv_content)
        textView?.setTextColor(ContextCompat.getColor(context, textColor))
    }

    fun setTextSize(textSize: Float) {
        val textView = getView<TextView>(R.id.slideLayout_tv_content)
        textView?.textSize = textSize
    }

    fun setBackgroundColor(context: Context, backgroundColor: Int, type: MenuType) {
        val layout: RelativeLayout?
        when (type) {
            MenuType.TEXT -> {
                layout = getView(R.id.slideLayout_rl_bg)
                layout?.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
            }
            MenuType.IMAGE -> {
                layout = getView(R.id.slideLayout_img_bg)
                layout?.setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
            }
        }
    }

    fun setImageRes(imgRes: Int) {
        val imageView = getView<ImageView>(R.id.slideLayout_iv_img)
        imageView?.setBackgroundResource(imgRes)
    }


    fun closeMenu(): SlideViewHolder {
        (getView<View>(R.id.slideLayout) as SlideLayout).adapter.closeOpenItem()
        return this
    }
}
