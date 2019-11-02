package com.showmethe.wanandroid.expand

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.circularreveal.CircularRevealLinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.showmethe.wanandroid.expand.ExpandMenuChildLayout
import showmethe.github.core.util.widget.setOnSingleClickListener


/**
 * Author: showMeThe
 * Update Time: 2019/11/2
 * Package Name:com.example.ken.kmvvm.expand
 */
open class ExpandParentBehavior(context: Context?, attrs: AttributeSet?)
    : CoordinatorLayout.Behavior<ExpandMenuChildLayout>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: ExpandMenuChildLayout, dependency: View): Boolean {
        return dependency is FloatingActionButton
    }



    override fun onDependentViewChanged(parent: CoordinatorLayout, child: ExpandMenuChildLayout, dependency: View): Boolean {
        if(dependency is FloatingActionButton){
            dependency.setOnClickListener {
                if(child.isHidden()){
                    child.toVisible()
                    dependency.isExpanded = true
                }else{
                    child.toInVisible()
                    dependency.isExpanded = false
                }
            }
            dependentViewChanged(parent, child, dependency)

        }
        return true
    }


    open fun dependentViewChanged(parent: CoordinatorLayout, child: CircularRevealLinearLayout, dependency: View){

    }
}