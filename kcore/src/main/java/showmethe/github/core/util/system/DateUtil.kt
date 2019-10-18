package showmethe.github.core.util.system

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * showmethe.github.core.util.system
 * ken
 * 2019/1/12
 **/



const val YYYYMMDDHHmm = "YYYY年MM月DD日 HH:mm"
const val YYYYMMDDHHmmss = "YYYY年MM月DD日 HH:mm:ss"

const val yyyy_MM_dd_HHmm = "yyyy-MM-dd HH:mm"
const val yyyy_MM_dd = "yyyy-MM-dd"
const val yyyyMMdd_HHmm = "yyyy/MM/dd HH:mm"


fun parseDateTime(time :  String , format : String ) : Date?{
    var style  = format
    if(style.isEmpty()){
        style = yyyy_MM_dd_HHmm
    }
    val sdf = SimpleDateFormat(format, Locale.CHINESE)
    sdf.timeZone = TimeZone.getTimeZone("GMT+8:00")
    var date : Date? = null
    try {
        date = sdf.parse(time)
    } catch (e : Exception ) {
        e.printStackTrace()
    }
    return date
}


fun getDateTime(time: Long, format: String): String {
    val nowDate = Date(time)
    val now = Calendar.getInstance()
    now.time = nowDate
    val formatter = SimpleDateFormat(format, Locale.CHINESE)
    return formatter.format(now.time)
}

fun parseToDateLong(time: String, format: String): Long {
    val sdf = SimpleDateFormat(format, Locale.CHINA)
    var date: Date? = null
    if (time.length < 5)
        return 0
    return try {
        date = sdf.parse(time)
        date.time
    } catch (e: ParseException) {
        e.printStackTrace()
        0
    }
}

fun getNowTime(): String {
    val nowDate = Date()
    val now = Calendar.getInstance()
    now.time = nowDate
    val formatter = SimpleDateFormat(yyyy_MM_dd_HHmm, Locale.CHINESE)
    return formatter.format(now.time)
}

fun getTimeDistance(startTime: Long, endTime: Long): String {
    var ret: String? = null
    val offset = endTime - startTime
    if (endTime == 0L) {
        ret = ""
    } else {
        if (offset > 0) {
            val day: Long = offset / (24 * 60 * 60 * 1000)
            val hour: Long
            val min: Long
            val sec: Long
            hour = offset / (60 * 60 * 1000) - day * 24
            min = offset / (60 * 1000) - day * 24 * 60 - hour * 60
            sec = offset / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60
            if (day != 0L) return (day * 24 + hour).toString() + ":" + min + ":" + sec + ""
            if (hour != 0L) return "$hour:$min:$sec"
            if (min != 0L) return "$min:$sec"
            return if (sec != 0L) sec.toString() + "" else "00:00:00"
        } else {
            ret = "00:00:00"
        }
    }
    return ret
}