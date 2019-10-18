package showmethe.github.core.util.match

import java.security.MessageDigest
import kotlin.experimental.and

fun MD5(s: String): String {
    var result = ""
    val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    try {
        val strTemp = s.toByteArray()
        val mdTemp = MessageDigest.getInstance("MD5")
        mdTemp.update(strTemp)
        val md = mdTemp.digest()
        val j = md.size
        val str = CharArray(j * 2)
        var k = 0
        for (i in 0 until j) {
            val b = md[i]
            str[k++] = hexDigits[(b.toInt().shr(4) and 0xf)]
            str[k++] = hexDigits[(b and 0xf).toInt()]
        }
        result = String(str)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

fun string2MD5(inStr: String): String {
    var md5: MessageDigest? = null
    try {
        md5 = MessageDigest.getInstance("MD5")
    } catch (e: Exception) {
        println(e.toString())
        e.printStackTrace()
        return ""
    }

    val charArray = inStr.toCharArray()
    val byteArray = ByteArray(charArray.size)

    for (i in charArray.indices)
        byteArray[i] = charArray[i].toByte()
    val md5Bytes = md5!!.digest(byteArray)
    val hexValue = StringBuffer()
    for (i in md5Bytes.indices) {
        val `val` = md5Bytes[i].toInt() and 0xff
        if (`val` < 16)
            hexValue.append("0")
        hexValue.append(Integer.toHexString(`val`))
    }
    return hexValue.toString()

}