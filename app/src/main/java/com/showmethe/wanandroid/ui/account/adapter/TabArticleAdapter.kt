package com.showmethe.wanandroid.ui.account.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ken.materialwanandroid.entity.TabItem

/**
 * com.ken.materialwanandroid.ui.main.adapter
 *
 * 2019/9/4
 **/
class TabArticleAdapter(var list : List<Fragment>, var titles:ArrayList<TabItem>, fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    override fun getPageTitle(position: Int): CharSequence? = titles[position].title
    override fun getCount(): Int = list.size
    override fun getItem(position: Int): Fragment = list[position]
}