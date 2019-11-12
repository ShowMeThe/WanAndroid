package com.showmethe.wanandroid.expand

import android.animation.*
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout

import androidx.core.content.ContextCompat

import com.google.android.material.circularreveal.CircularRevealLinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.showmethe.speeddiallib.expand.ExpandIcon
import com.showmethe.wanandroid.R

import kotlin.collections.ArrayList

/**
 * Author: showMeThe
 * Update Time: 2019/11/11 11:39
 * Package Name:com.showmethe.speeddiallib.expand
 */
class ExpandMenuChildLayout(context: Context, attrs: AttributeSet?) : CircularRevealLinearLayout(context, attrs) {


    private lateinit var builder: Builder
    private val fabs = ArrayList<FloatingActionButton>()
    private val textLabel = ArrayList<MaterialTextView>()
    private val parentLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
    private val showSet  = ArrayList<AnimatorSet>()
    private val hideSet  =  ArrayList<AnimatorSet>()
    private var isHide = true
    private var targetCount = 0
    private var showCount = 0
    private var originBackTint : ColorStateList? = null
    private var originIcon : Drawable?  = null
    private var originIconTint : ColorStateList? = null
    private val showMotionSet = AnimatorSet()
    private val hideMotionSet = AnimatorSet()

    fun isHidden() = isHide

    fun setBuilder(builder: Builder){
        this.builder = builder
        targetCount = builder.getExpandIcons().size
        init()
    }

    private fun init(){

        for((index,expandIcon) in  builder.getExpandIcons().withIndex()){
            fabs.add(createFab(index, expandIcon))
            textLabel.add(createTextLabel(index, expandIcon))
        }

        when(builder.getSlide()){
            Builder.Slide.TOP ->{
                addTopMenu()
            }
            Builder.Slide.BOTTOM ->{
                addBottomMenu()
            }
        }
    }


    private fun createFab(index: Int,expandIcon: ExpandIcon) : FloatingActionButton{
        val fab = FloatingActionButton(context)
        if(expandIcon.getIcon()!=-1){
            fab.setImageResource(expandIcon.getIcon())
        }
        fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,expandIcon.getBackgroundTint()))
        fab.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,expandIcon.getIconTint()))
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        fab.size = FloatingActionButton.SIZE_MINI
        layoutParams.bottomMargin = 15
        layoutParams.topMargin = 15
        fab.layoutParams = layoutParams
        fab.visibility = View.INVISIBLE
        fab.scaleX = 0f
        fab.scaleY = 0f
        fab.alpha = 0f
        fab.compatElevation = 8f
        val listener = OnClickListener {
            onMenuClick?.invoke(index)
        }
        fab.setOnClickListener(listener)
        return fab
    }


    private fun createTextLabel(index: Int,expandIcon: ExpandIcon) : MaterialTextView{
        val textView = MaterialTextView(context)
        textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white_85))
        textView.text = expandIcon.getTextLabel()
        textView.setTextColor(Color.parseColor("#333333"))
        textView.setPadding(10,10,10,10)
        textView.elevation = 8f
        val layoutParams = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
        layoutParams.marginEnd = 15
        textView.layoutParams = layoutParams
        textView.textSize = 12f
        textView.maxLines = 1
        textView.maxEms = 10
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.visibility = View.INVISIBLE
        return textView
    }


    fun toVisible(it :View){
        if(fabs.isEmpty() || !isHide || showCount != 0){
            return
        }
        it.isEnabled = false
        visibility = View.VISIBLE
        if(showSet.isEmpty()){
           for((index,fab)  in fabs.withIndex()){
               createShow(index, fab,it)
           }
            for(animator in showSet){
                animator.start()
            }
       }
        for(animator in showSet){
            animator.start()
        }
    }


    fun toInVisible(it :View){
        if(fabs.isEmpty() || isHide || showCount == 0){
            return
        }
        it.isEnabled = false
        if(hideSet.isEmpty()){
            for((index,fab)  in fabs.withIndex()){
                createHide(index, fab,it)
            }
            for(animator in hideSet){
                animator.start()
            }
        }
        for(animator in hideSet){
            animator.start()
        }
    }

    fun createMotion(fab: FloatingActionButton){
       if(builder.getMotionIcon()!=-1){
           if(isHidden()){
               createSave(fab)
               createShowMotion(fab)
           }else{
               createHideMotion(fab)
           }
       }
    }


    private fun createSave(fab: FloatingActionButton){
        originBackTint = fab.backgroundTintList
        originIcon = fab.drawable
        originIconTint = fab.imageTintList
    }

    private fun reverseSave(fab: FloatingActionButton){
         fab.backgroundTintList = originBackTint
         fab.setImageDrawable(originIcon)
         fab.imageTintList = originIconTint
    }

    private fun createShowMotion(fab: FloatingActionButton){
       if(showMotionSet.childAnimations.isEmpty()){
           val alpha = ObjectAnimator.ofFloat(fab,"alpha",0f,0.75f ,0.5f,0.75f,1.0f)
           val rotate = ObjectAnimator.ofFloat(fab,"rotation",0f,180f)
           val scaleX = ObjectAnimator.ofFloat(fab,"scaleX",1.0f,0.75f,1.0f)
           val scaleY = ObjectAnimator.ofFloat(fab,"scaleY",1.0f,0.75f,1.0f)
           val color = ValueAnimator.ofObject(ArgbEvaluator(),fab.backgroundTintList!!.defaultColor,
               ContextCompat.getColor(context,builder.getMotionColor()))
           color.addUpdateListener {
               fab.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
           }
           showMotionSet.interpolator = LinearInterpolator()
           showMotionSet.duration = 450
           showMotionSet.playTogether(alpha,color,rotate,scaleX,scaleY)
           val newBackgroundTint = ContextCompat.getColorStateList(context,builder.getMotionColor())
           val newDrawable = context.getDrawable(builder.getMotionIcon())
           showMotionSet.addListener(object : AnimatorListenerAdapter(){
               override fun onAnimationStart(animation: Animator?) {

               }
               override fun onAnimationEnd(animation: Animator?) {
                   fab.backgroundTintList = newBackgroundTint
                   fab.setImageDrawable(newDrawable)

               }
           })
           showMotionSet.start()
       }else{
           showMotionSet.start()
       }

    }


    private fun createHideMotion(fab: FloatingActionButton){
        if(hideMotionSet.childAnimations.isEmpty()){
            val alpha = ObjectAnimator.ofFloat(fab,"alpha",0f,0.75f ,0.5f,0.75f,1.0f)
            val rotate = ObjectAnimator.ofFloat(fab,"rotation",180f,0f)
            val scaleX = ObjectAnimator.ofFloat(fab,"scaleX",1.0f,0.75f,1.0f)
            val scaleY = ObjectAnimator.ofFloat(fab,"scaleY",1.0f,0.75f,1.0f)
            val color = ValueAnimator.ofObject(ArgbEvaluator(),ContextCompat.getColor(context,builder.getMotionColor()),
                fab.backgroundTintList!!.defaultColor)
            color.addUpdateListener {
                fab.backgroundTintList = ColorStateList.valueOf(it.animatedValue as Int)
            }
            hideMotionSet.interpolator = LinearInterpolator()
            hideMotionSet.duration = 450
            hideMotionSet.playTogether(alpha,rotate,scaleX,scaleY,color)
            hideMotionSet.addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    reverseSave(fab)
                }
            })
            hideMotionSet.start()
        }else{
            hideMotionSet.start()
        }
    }

    private fun createShow(index: Int,fab : FloatingActionButton,it:View){
        val alpha = ObjectAnimator.ofFloat(fab,"alpha",0f,1.0f)
        val scaleX = ObjectAnimator.ofFloat(fab,"scaleX",0f,1.0f)
        val scaleY = ObjectAnimator.ofFloat(fab,"scaleY",0f,1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = 350
        animatorSet.startDelay = index * 150 + 50L
        animatorSet.playTogether(alpha,scaleX,scaleY)
        val listenerAdapter = object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                fab.visibility  = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator?) {
                if(textLabel[index].text.isNotEmpty()){
                    textLabel[index].visibility = View.VISIBLE
                }
                showCount ++
                if(showCount == targetCount){
                    isHide = false
                    it.isEnabled = true
                }
            }
        }
        animatorSet.addListener(listenerAdapter)
        showSet.add(animatorSet)
    }


    private fun createHide(index: Int,fab : FloatingActionButton,it:View){
        val alpha = ObjectAnimator.ofFloat(fab,"alpha",1f,0.0f)
        val scaleX = ObjectAnimator.ofFloat(fab,"scaleX",1f,0.0f)
        val scaleY = ObjectAnimator.ofFloat(fab,"scaleY",1f,0.0f)
        val animatorSet = AnimatorSet()
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.duration = 350
        animatorSet.startDelay = index * 80 + 50L
        animatorSet.playTogether(alpha,scaleX,scaleY)
        val listenerAdapter = object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                fab.visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator?) {
                fab.visibility = View.INVISIBLE
                textLabel[index].visibility = View.INVISIBLE
                showCount--
                if(showCount == 0){
                    isHide = true
                    it.isEnabled = true
                }
            }
        }
        animatorSet.addListener(listenerAdapter)
        hideSet.add(animatorSet)
    }


    private fun addTopMenu(){
        orientation = LinearLayout.VERTICAL
        fabs.reverse()
        for((index,fab) in fabs.withIndex()){
            val container = LinearLayout(context)
            container.orientation = LinearLayout.HORIZONTAL
            parentLayoutParams.gravity =  Gravity.END
            container.gravity = Gravity.CENTER
            container.addView(textLabel[index])
            container.addView(fab)
            addView(container,parentLayoutParams)
        }

    }


    private fun addBottomMenu(){
        orientation = LinearLayout.VERTICAL
        //fabs.reverse()
        for((index,fab) in fabs.withIndex()){
            val container = LinearLayout(context)
            container.orientation = LinearLayout.HORIZONTAL
            parentLayoutParams.gravity =  Gravity.END
            container.gravity = Gravity.CENTER
            container.addView(textLabel[index])
            container.addView(fab)
            addView(container,parentLayoutParams)
        }

    }

    private var onMenuClick:((index:Int)->Unit)? = null
    fun setOnMenuClickListener(onMenuClick:((index:Int)->Unit)){
        this.onMenuClick = onMenuClick
    }


}