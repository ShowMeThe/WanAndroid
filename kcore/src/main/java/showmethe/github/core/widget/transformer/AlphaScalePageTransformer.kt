package showmethe.github.core.widget.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:50
 * Package Name:showmethe.github.core.widget.transformer
 */
class AlphaScalePageTransformer(scale : Float = 0.8f,alpha : Float = 0.5f) : ViewPager2.PageTransformer {

    private val SCALE_MAX = scale
    private val ALPHA_MAX = alpha

    override fun transformPage(page: View, position: Float) {
        val scale = if (position < 0)
            (1 - SCALE_MAX) * position + 1
        else
            (SCALE_MAX - 1) * position + 1
        val alpha = if (position < 0)
            (1 - ALPHA_MAX) * position + 1
        else
            (ALPHA_MAX - 1) * position + 1
        if (position < 0) {
            page.pivotX = page.width.toFloat()
            page.pivotY = (page.height / 2).toFloat()
        } else {
            page.pivotX = 0f
            page.pivotY = (page.height / 2).toFloat()
        }
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = Math.abs(alpha)
    }
}
