package com.showmethe.galley.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import showmethe.github.core.widget.common.AutoRecyclerView
import java.lang.ref.WeakReference


/**
 * Author: showMeThe
 * Update Time: 2019/10/18
 * Package Name:com.showmethe.galley.widget
 */
class ScrollRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr),DefaultLifecycleObserver{

    private var lastPosition = 0
    private var firstPosition = 0
    private var  totalCountSize = 0
    private var onBottom = false
    private var step = 0
    private var totalY = 0
    private val delayTime = 150
    private  val interpolator = LinearInterpolator()
    private val mHandler = WeakReference(Handler(Looper.getMainLooper())).get()!!

    private val task = object : Runnable{
        override fun run() {
            if(!onBottom){
                smoothScrollBy(0,step,interpolator)
            }else{
                smoothScrollBy(0,  -step ,interpolator)
            }
            mHandler.postDelayed(this,delayTime.toLong())
        }
    }


    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
            val layoutManager = layoutManager
            totalCountSize = layoutManager!!.itemCount
            when (layoutManager) {
                is GridLayoutManager -> {
                    lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition()

                    if(firstPosition == 0){
                        onBottom = false
                    }
                    if(lastPosition == totalCountSize -1){
                        onBottom = true
                    }
                }
            }
    }


    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        totalY = measuredHeight
        step = totalY/20
    }

    private fun toNextY(){
        mHandler.postDelayed(task, delayTime.toLong())
    }

    fun bindLife(owner: LifecycleOwner){
        owner.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        stop()
        play()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stop()
    }


    private fun play(){
        toNextY()
    }

    private fun stop(){
        mHandler.removeCallbacks(task)
    }



}