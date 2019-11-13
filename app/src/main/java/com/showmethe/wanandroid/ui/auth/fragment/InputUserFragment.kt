package com.showmethe.wanandroid.ui.auth.fragment

import android.os.Bundle
import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentInputBinding
import showmethe.github.core.base.BaseFragment

/**
 * Author: showMeThe
 * Update Time: 2019/11/13 16:12
 * Package Name:com.showmethe.wanandroid.ui.auth.fragment
 */
class InputUserFragment  : BaseFragment<FragmentInputBinding, AuthViewModel>() {
    override fun initViewModel(): AuthViewModel = createViewModel(AuthViewModel::class.java)

    override fun getViewId(): Int = R.layout.fragment_input

    override fun onBundle(bundle: Bundle) {


    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
    }

    override fun initListener() {
    }
}