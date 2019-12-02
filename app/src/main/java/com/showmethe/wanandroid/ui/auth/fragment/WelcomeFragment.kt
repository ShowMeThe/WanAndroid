package com.showmethe.wanandroid.ui.auth.fragment

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import com.google.android.material.circularreveal.CircularRevealCompat
import com.google.android.material.circularreveal.CircularRevealWidget
import com.showmethe.wanandroid.ui.auth.vm.AuthViewModel
import com.showmethe.wanandroid.R
import com.showmethe.wanandroid.constant.HAS_LOGIN
import com.showmethe.wanandroid.databinding.FragmentWelcomeBinding
import com.showmethe.wanandroid.ui.auth.LoginActivity
import com.showmethe.wanandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_welcome.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.onGlobalLayout
import showmethe.github.core.util.rden.RDEN
import java.time.Duration
import kotlin.math.hypot

/**
 * Author: showMeThe
 * Update Time: 2019/11/13 15:44
 * Package Name:com.showmethe.wanandroid.ui.auth.fragment
 */
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding, AuthViewModel>() {

    private val hasLogin = RDEN.get(HAS_LOGIN,false)
    override fun initViewModel(): AuthViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_welcome

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this
        initAnim()
        if(hasLogin){
            tvTop.text = "欢迎回来玩安卓"
        }
    }

    override fun onVisible() {
        reset()
        toAnim(0,500)
    }

    override fun onHidden() {

    }

    override fun initListener() {


    }


    fun startReg(){
        viewModel.toNext.value = 1
    }


    fun startToMain(){
        val intent = Intent(context,LoginActivity::class.java)
        context.startActivity(intent)
        context.overridePendingTransition(R.anim.anim_right_in,R.anim.anim_left_out)
    }


    private fun initAnim(){

        val revealDuration = 900L
        val logoDuration = 500L

        card.onGlobalLayout {
            val centerX = measuredWidth/2f
            val centerY = measuredHeight/2f
            val finalRadius = hypot(
                centerX.coerceAtLeast(measuredWidth - centerX),
                centerY.coerceAtLeast(measuredHeight - centerY)
            )
            val reveal = CircularRevealWidget.RevealInfo(centerX,centerX,0f)
            card.revealInfo = reveal
            val circularReveal = CircularRevealCompat.createCircularReveal(card,centerX,centerY,finalRadius)
            circularReveal.duration = revealDuration
            circularReveal.startDelay = 50
            circularReveal.interpolator = LinearInterpolator()
            circularReveal.start()
        }

        toAnim(revealDuration, logoDuration)
    }


    private fun toAnim(revealDuration : Long,logoDuration:Long){
        tvTop.animate()
            .scaleY(1.0f)
            .scaleX(1.0f)
            .alpha(1.0f)
            .setInterpolator(AnticipateOvershootInterpolator())
            .translationY(100f)
            .setDuration(logoDuration)
            .setStartDelay(revealDuration + logoDuration)
            .start()

        tvCenter.animate()
            .scaleY(1.0f)
            .scaleX(1.0f)
            .alpha(1.0f)
            .setInterpolator(AnticipateOvershootInterpolator())
            .translationY(100f)
            .setDuration(logoDuration)
            .setStartDelay(revealDuration + logoDuration)
            .start()


        if(!hasLogin){
            btnReg.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .alpha(1.0f)
                .setInterpolator(AnticipateOvershootInterpolator())
                .translationY(30f)
                .setDuration(logoDuration)
                .setStartDelay(revealDuration + logoDuration*2)
                .start()

            tvLogin.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .alpha(1.0f)
                .setInterpolator(AnticipateOvershootInterpolator())
                .setDuration(logoDuration)
                .setStartDelay(revealDuration + logoDuration*2)
                .start()
        }else{
            tvLogin.postDelayed({
                context.startActivity<MainActivity>()
            },3000)
        }
    }


    private fun reset(){
        tvTop.apply {
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            translationY = -50f
        }
        tvCenter.apply {
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            translationY = -50f
        }
        btnReg.apply {
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            translationY = -250f
        }
        tvLogin.apply {
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
        }

    }


}