package showmethe.github.core.widget.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:50
 * Package Name:showmethe.github.core.widget.transformer
 */
class ParallaxTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val width = page.width
        if (position < -1) {
            page.scrollX = (width.toDouble() * 0.75 * -1.0).toInt()
        } else if (position <= 1) {
            page.scrollX = (width.toDouble() * 0.75 * position.toDouble()).toInt()
        } else {
            page.scrollX = (width * 0.75).toInt()
        }
    }
}
