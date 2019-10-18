package showmethe.github.core.widget.banner

import android.content.Context
import android.widget.ImageView

import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

import showmethe.github.core.R
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.databinding.ItemBannerViewBinding

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:54
 * Package Name:showmethe.github.core.widget.banner
 */
class BannerViewAdapter(context: Context, data: ObservableArrayList<Any>) : DataBindBaseAdapter<Any, ItemBannerViewBinding>(context, data) {

    private var imageLoader: onImageLoader? = null

    override fun bindItems(binding: ItemBannerViewBinding?, item: Any, position: Int) {
        if (binding != null) {
            if (imageLoader != null) {
                imageLoader!!.display(item, binding.ivBanner)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.itemAnimator as SimpleItemAnimator).removeDuration = 0
        recyclerView.itemAnimator = null
    }

    interface onImageLoader {

        fun display(url: Any, imageView: ImageView)

    }

    fun setOnImageLoader(loader: onImageLoader) {
        imageLoader = loader
    }

    override fun getItemLayout(): Int {
        return R.layout.item_banner_view
    }
}
