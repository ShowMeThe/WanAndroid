package com.showmethe.wanandroid.expand

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.circularreveal.CircularRevealLinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * Author: showMeThe
 * Update Time: 2019/11/11 11:40
 * Package Name:com.showmethe.speeddiallib.expand
 */
open class ExpandParentBehavior(context: Context?, attrs: AttributeSet?)
    : CoordinatorLayout.Behavior<ExpandMenuChildLayout>(context, attrs) {

    private var lock = false //防止重复设置点击事件
    override fun layoutDependsOn(parent: CoordinatorLayout, child: ExpandMenuChildLayout, dependency: View): Boolean {
        if(dependency is FloatingActionButton){
            return (child.layoutParams as CoordinatorLayout.LayoutParams).anchorId == dependency.id
        }else{
            return false
        }
    }


    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ExpandMenuChildLayout, dependency: View): Boolean {
        if(dependency is FloatingActionButton){
            if(!lock){
                dependency.setOnSingleClickListener {
                    if(child.isHidden()){
                        child.toVisible(dependency)
                        child.createMotion(dependency)
                        dependency.isExpanded = true
                    }else{
                        child.toInVisible(dependency)
                        child.createMotion(dependency)
                        dependency.isExpanded = false
                    }
                }
                lock = true
            }
            dependentViewChanged(parent, child, dependency)

        }
        return true
    }


    open fun dependentViewChanged(parent: CoordinatorLayout, child: CircularRevealLinearLayout, dependency: View){

    }


}
//过滤疯狂点击
private fun View.setOnSingleClickListener(onSingleClick : (it:View)->Unit){
    var lastClickTime = 0L
    val lastLongTime = 500L
    setOnClickListener {
        val time = System.currentTimeMillis()
        if(time - lastClickTime > lastLongTime){
            onSingleClick.invoke(it)
            lastClickTime = time
        }
    }
}