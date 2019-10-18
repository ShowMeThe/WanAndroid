package showmethe.github.core.util.extras


import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.extras
 */


/**
 * 保留两位小数
 * @param num
 * @return
 */
fun double2Decimal(num: Double): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    df.groupingSize = 0
    df.roundingMode = RoundingMode.FLOOR
    val style = "###0.00"// 定义要显示的数字的格式
    df.applyPattern(style)
    return df.format(num)
}

/**
 * 保留两位小数
 * @param num
 * @return
 */
fun float2Decimal(num: Float): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    df.groupingSize = 0
    df.roundingMode = RoundingMode.FLOOR
    val style = "###0.00"// 定义要显示的数字的格式
    df.applyPattern(style)
    return df.format(num.toDouble())
}