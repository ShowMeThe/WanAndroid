package com.showmethe.galley.ui.home.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.showmethe.galley.R
import com.showmethe.galley.databinding.ItemVerticalBinding
import showmethe.github.core.adapter.DataBindBaseAdapter

/**
 * Author: showMeThe
 * Update Time: 2019/10/18
 * Package Name:com.showmethe.galley.ui.home.adapter
 */
class VerticalAdapter(context: Context, data: ObservableArrayList<String>) :
    DataBindBaseAdapter<String, ItemVerticalBinding>(context, data) {
    override fun getItemLayout(): Int = R.layout.item_vertical

    override fun bindItems(binding: ItemVerticalBinding?, item: String, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()
        }
    }
}