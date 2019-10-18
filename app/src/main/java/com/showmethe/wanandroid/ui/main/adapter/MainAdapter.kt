package com.showmethe.wanandroid.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * com.ken.materialwanandroid.ui.main.adapter
 *
 * 2019/9/4
 **/
class MainAdapter(var list : List<Fragment>,fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    override fun getCount(): Int =list.size
    override fun getItem(position: Int): Fragment = list[position]
}