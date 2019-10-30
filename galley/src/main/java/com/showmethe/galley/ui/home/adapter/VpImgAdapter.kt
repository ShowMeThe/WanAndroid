package com.showmethe.galley.ui.home.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import com.showmethe.galley.R
import com.showmethe.galley.databinding.ItemVpImgBinding
import okhttp3.internal.toHexString
import okhttp3.internal.wait
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.glide.TGlide
import showmethe.github.core.util.match.isEqual
import java.lang.ref.WeakReference

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 17:13
 * Package Name:com.showmethe.galley.ui.home.adapter
 */
class VpImgAdapter(context: Context, data: ObservableArrayList<String>) :
    DataBindBaseAdapter<String, ItemVpImgBinding>(context, data) {
    override fun getItemLayout(): Int  = R.layout.item_vp_img

    override fun bindItems(binding: ItemVpImgBinding?, item: String, position: Int) {
        binding?.apply {
            // bean = item
            TGlide.loadProgress(item, WeakReference(iv))
            executePendingBindings()
        }
    }

}