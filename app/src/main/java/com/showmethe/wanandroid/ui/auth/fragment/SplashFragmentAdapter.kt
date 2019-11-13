package com.showmethe.wanandroid.ui.auth.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Author: showMeThe
 * Update Time: 2019/11/13 15:58
 * Package Name:com.showmethe.wanandroid.ui.auth.fragment
 */
class SplashFragmentAdapter(var list: ArrayList<Fragment>,fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]
}