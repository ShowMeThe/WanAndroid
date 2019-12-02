package com.showmethe.wanandroid.ui.auth.fragment

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.databinding.FragmentInputPswBinding
import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel
import kotlinx.android.synthetic.main.fragment_input_psw.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.Interval
import showmethe.github.core.util.extras.SimpleTextWatcher

/**
 * com.showmethe.wanandroid.ui.auth.fragment
 * 2019/11/14
 **/
class InputPswFragment : BaseFragment<FragmentInputPswBinding, AuthViewModel>() {

    private var isShow = false


    override fun initViewModel(): AuthViewModel = createViewModel()

    override fun getViewId(): Int =  R.layout.fragment_input_psw

    override fun onBundle(bundle: Bundle) {

    }

    override fun onHidden() {
        reset()
    }

    override fun onVisible() {
        onShow()
    }

    override fun observerUI() {


    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this
        binding?.register = viewModel.registerBean


    }




    override fun initListener() {

        edPswd.addTextChangedListener(object : SimpleTextWatcher(){
            override fun afterTextChanged(p0: Editable?) {
                if(!p0.isNullOrEmpty()){
                    btnReg.visibility  = View.VISIBLE
                }else{
                    btnReg.visibility  = View.INVISIBLE
                }
            }
        })

    }

    fun startReg(){
        router.toTarget("register")
    }


    fun backToTop(){
        viewModel.toNext.value = 1
        reset()
    }




    private fun onShow(){


        tvBack.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(500)
            .setInterpolator(AnticipateOvershootInterpolator())
            .setStartDelay(250)
            .start()
    }

    private fun reset(){
        tvBack.apply {
            scaleY = 0f
            scaleX = 0f
        }
    }

}