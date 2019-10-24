package com.showmethe.wanandroid.ui.main.adapter

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemImageViewBinding
import com.showmethe.wanandroid.util.HoldViewHelper
import showmethe.github.core.adapter.DataBindBaseAdapter


class ImageViewAdapter(context: Context, data: ObservableArrayList<String>) :
    DataBindBaseAdapter<String, ItemImageViewBinding>(context, data) {
    override fun getItemLayout(): Int = R.layout.item_image_view

    override fun bindItems(binding: ItemImageViewBinding?, item: String, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()


            image.setOnExitListener { view, translateX, translateY, w, h ->
                onFinish?.invoke()
            }
            image.setOnAlphaListener {
                HoldViewHelper.get().setHoldViewAlpha(1f)
                bg.alpha = it/255
            }

        }
    }

    private var onFinish:(()->Unit)? = null
    fun setOnFinishListener( onFinish:(()->Unit)){
        this.onFinish  = onFinish
    }

}