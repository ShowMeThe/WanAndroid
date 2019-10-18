package showmethe.github.core.widget.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:50
 * Package Name:showmethe.github.core.widget.transformer
 */
class SpinnerTransformer(var rotate:Int = 1): ViewPager2.PageTransformer {


    override fun transformPage(page: View, position: Float) {
        page.translationX = (-position * page.width)
        page.cameraDistance = 6000f
        if(position<0.5 && position>-0.5){
            page.visibility  = View.VISIBLE
        }else{
            page.visibility = View.INVISIBLE
        }

        if(position<-1){
            page.alpha = 0.8f
        }else if(position<=0){
            page.alpha = 1f
            page.rotationY = (180 * rotate * (1 - Math.abs(position)+1))
        }else if(position <=1){
            page.alpha = 1f
            page.rotationY = (-180 * rotate * (1 - Math.abs(position)+1))
        }else{
            page.alpha = 0.8f
        }
    }
}