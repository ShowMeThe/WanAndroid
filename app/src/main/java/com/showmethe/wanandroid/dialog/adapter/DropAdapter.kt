package com.showmethe.wanandroid.dialog.adapter


import android.content.Context
import androidx.databinding.ObservableArrayList



import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemCateTabBinding
import com.showmethe.wanandroid.entity.CateTab
import showmethe.github.core.adapter.DataBindBaseAdapter


class DropAdapter(context: Context, data: ObservableArrayList<CateTab>) :
    DataBindBaseAdapter<CateTab, ItemCateTabBinding>(context, data) {
    override fun getItemLayout(): Int = R.layout.item_cate_tab

    override fun bindItems(binding: ItemCateTabBinding?, item: CateTab, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()
        }
    }
}