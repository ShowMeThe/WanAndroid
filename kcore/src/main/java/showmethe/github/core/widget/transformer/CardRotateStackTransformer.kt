package showmethe.github.core.widget.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:50
 * Package Name:showmethe.github.core.widget.transformer
 */
class CardRotateStackTransformer  : ViewPager2.PageTransformer{

    override fun transformPage(page: View, position: Float) {
        if (position <= 0) {
            page.translationX = -position * page.width
            page.rotation = -45 * position
        }else{
            page.rotation = 45 * position
            page.translationX = (page.width / 2 * position)
        }
    }


}
