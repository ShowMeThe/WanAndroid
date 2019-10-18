package showmethe.github.core.widget.transformer

import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:50
 * Package Name:showmethe.github.core.widget.transformer
 */
class CardStackTransformer (var offset : Int = 20) : ViewPager2.PageTransformer {

    val interpolator = AccelerateInterpolator(3f)

    override fun transformPage(page: View, position: Float) {

        page.scaleX = 0.95f
        page.scaleY = 0.95f
        if(position == 0f){
            page.alpha = 1f
            page.rotation = 0f
            page.translationX = 0.0f
        }else if(position>0){
            page.translationX = -position * (page.width - offset)
            page.translationY = position * offset
            page.translationZ = -position
            page.alpha = (1f - 0.35f * position)
        }else{
            page.rotation = position*45
            page.alpha = (1f - 0.35f * Math.abs(position))
            page.translationX = position * page.width/2
            page.scaleY = 0.95f - Math.abs(position)*0.65f
            page.scaleX = 0.95f -  Math.abs(position)*0.65f
        }
    }
}