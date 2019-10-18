package showmethe.github.core.util.match

import java.util.regex.Pattern


/**
 * 判断两个是否相同
 */
fun Any.isEqual(any: Any): Boolean {
    return this == any
}



/**
 * 判断对象是否为空 或者字符串写入"null"
*/
fun Any.isNotNull(): Boolean {
    val str = this.toString() + ""
    return ("" != str && "null" != str)
}

/**
 * 同时判断多个参数是否为空
 *
 * @param strArray
 * @return
 */
fun isNull(vararg strArray: Any): Boolean {
    for (obj in strArray) {
        if ("" != obj.toString() + "") {
            return false
        }
    }
    return true
}

/**
 * 判是否是字母
 *
 * @return
 */
fun String.isLetter(): Boolean {
    if (isNull(this)) {
        return false
    }
    val p = Pattern.compile("[a-zA-Z]")
    val m = p.matcher(this)
    return m.matches()
}




/**
 * 检测是否有emoji表情
 *
 * @param source
 * @return
 */

fun String.containsEmoji(): Boolean {
    val len = this.length
    for (i in 0 until len) {
        val codePoint = this[i]
        if (!codePoint.isEMOJICharacter()) {
            return true
        }
    }
    return false
}


/**
 * 判断是否是Emoji
 *
 * @param codePoint 比较的单个字符
 * @return
 */

private fun Char.isEMOJICharacter(): Boolean {
    return (toInt() == 0x0
            || toInt() == 0x9
            || toInt() == 0xA
            || toInt() == 0xD
            || toInt() in 0x20..0xD7FF || toInt() in 0xE000..0xFFFD
            || toInt() in 0x10000..0x10FFFF)
}

/**
 * 判断是否邮箱
 *
 * @param strObj
 * @return
 */
fun String.checkEmail(): Boolean {
    val str = toString() + ""
    if (!str.endsWith(".com")) {
        return false
    }
    val match = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*"
    val pattern = Pattern.compile(match)
    val matcher = pattern.matcher(str)
    return matcher.matches()
}


/**
 * 监测是否为正确的手机号码
 *
 * @param mobile
 * @return
 */
fun isMobileCorrect(mobile: String): Boolean {
    val regex = "^((13[0-9])|(15[^4])|(16[0-9])|(17[0-8])|(18[0-9])|(19[0-9])|(147,145))\\d{8}$"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(mobile)
    return matcher.matches()
}


/**
 * 功能：判断字符串是否为数字
 *
 * @param str
 * @return
 */
private fun String.isNumeric(): Boolean {
    val pattern = Pattern.compile("[0-9]*")
    val isNum = pattern.matcher(this)
    return isNum.matches()
}

/**
 * 功能：判断字符串是否为日期格式
 *
 * @param
 * @return
 */
fun String.isDate(): Boolean {
    val pattern = Pattern
        .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$")
    val m = pattern.matcher(this)
    return m.matches()
}

/**
 * 判断是否为min到max位的字母或数字
 *
 * @param s
 * @param min
 * @param max
 * @return
 */
fun isAlphanumericRange(s: String, min: Int, max: Int): Boolean {
    val regex = "^[a-z0-9A-Z]{$min,$max}$"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(s)
    return matcher.matches()
}


/**
 * 判断是否为n为数字的验证码
 *
 * @param s
 * @param n
 * @return
 */
fun isVerificationCode(s: String, n: Int): Boolean {
    val regex = "^[0-9]{$n}$"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(s)
    return matcher.matches()
}


/**
 * 检测文件链接
 * @param url
 * @return
 */
fun String.checkFileURL(): Boolean {
    if (isNull(this)) {
        return false
    }
    val regular = "(file)://[\\S]*"
    return Pattern.matches(regular, this)
}

/**
 * 检查是否合法金额
 *
 * @param goal
 * @return
 */
fun String.checkMoneyFormat(): Boolean {
    val regular = "^(([1-9]\\d*)|([0]))(\\.(\\d){0,2})?$"
    return Pattern.matches(regular, this)
}

