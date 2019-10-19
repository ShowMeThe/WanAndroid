package showmethe.github.core.util.widget

import android.content.Context
import kotlin.math.roundToInt


/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 *
 * @param pxValue
 * （DisplayMetrics类中属性density）
 * @return
 */
fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 *
 * @param dipValue
 * （DisplayMetrics类中属性density）
 * @return
 */
fun dip2px(context: Context, dipValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}


fun dp2px(context: Context, dipValue: Float): Float {
    return (context.resources.displayMetrics.density * dipValue)
}


/**
 * 将px值转换为sp值，保证文字大小不变
 *
 * @param pxValue
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun px2sp(context: Context, pxValue: Float): Float {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f)
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param spValue
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun sp2px(context: Context, spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}