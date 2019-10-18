package com.showmethe.wanandroid.ui.home.adapter


import android.content.Context
import androidx.databinding.ObservableArrayList


import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.ItemHomeSearchBinding
import com.showmethe.wanandroid.entity.HomeArticle
import showmethe.github.core.adapter.AutoLoadAdapter

class SearchAdapter(context: Context, data: ObservableArrayList<HomeArticle.DatasBean>) :
    AutoLoadAdapter<HomeArticle.DatasBean, ItemHomeSearchBinding>(context, data) {
    override fun bindItems(binding: ItemHomeSearchBinding?, item: HomeArticle.DatasBean, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()
        }
    }

    override fun getItemLayout(): Int  = R.layout.item_home_search

}
