package com.showmethe.wanandroid.ui.auth.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentInputBinding
import kotlinx.android.synthetic.main.fragment_input.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.SimpleTextWatcher

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
        binding?.main = this
        binding?.register = viewModel.registerBean
    }

    override fun onVisible() {
        onShow()
    }

    override fun initListener() {

        edName.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.apply {
                    tvNumber.text = "${p0.length}/10"
                }
            }
        })
    }



    fun toNextPage(){
       if(viewModel.registerBean.account.isNotEmpty()){
           viewModel.toNext.value = 2
       }
    }


    fun backToTop(){
        viewModel.toNext.value = 0
        reset()
    }

    private fun onShow(){
        tvNext.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(500)
            .setInterpolator(AnticipateOvershootInterpolator())
            .setStartDelay(150)
            .start()

        tvBack.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(500)
            .setInterpolator(AnticipateOvershootInterpolator())
            .setStartDelay(250)
            .start()
    }


    private fun reset(){
        tvNext.apply {
            scaleX = 0f
            scaleY = 0f
        }
        tvBack.apply {
            scaleY = 0f
            scaleX = 0f
        }
    }

}