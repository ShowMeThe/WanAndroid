package com.showmethe.wanandroid.expand

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.circularreveal.CircularRevealLinearLayout

/**
 * Author: showMeThe
 * Update Time: 2019/11/12 10:49
 * Package Name:com.showmethe.speeddiallib.expand
 */
class ExpandTopBehavior (context: Context?, attrs: AttributeSet?) : ExpandParentBehavior(context, attrs) {

    override fun dependentViewChanged(parent: CoordinatorLayout, child: CircularRevealLinearLayout, dependency: View) {
        val dependencyWidth = dependency.measuredWidth
        val dependencyHeight = dependency.measuredHeight
        val childHeight = child.measuredHeight
        val childWidth = child.measuredWidth
        val dependencyX = dependency.x
        val dependencyY = dependency.y + dependencyHeight

        child.x = dependencyX + dependencyWidth - childWidth
        child.y = dependencyY + ExpandMenuConfig.defaultMargin

    }



}