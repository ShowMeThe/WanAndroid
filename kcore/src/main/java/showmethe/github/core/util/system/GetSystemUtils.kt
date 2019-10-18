package showmethe.github.core.util.system

import android.os.Build
import android.os.Environment

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.system
 */
object GetSystemUtils {


    val SYS_EMUI = "sys_emui"
    val SYS_MIUI = "sys_miui"
    val SYS_FLYME = "sys_flyme"
    val SYS_OPPO = "sys_OPPO"
    private val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
    private val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
    private val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"
    private val KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level"
    private val KEY_EMUI_VERSION = "ro.build.version.emui"
    private val KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion"

    //小米
    //华为
    //魅族
    //OPPO
    val system: String
        get() {
            var SYS = ""
            try {
                val prop = Properties()
                prop.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
                if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                        || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                        || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                    SYS = SYS_MIUI
                } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                        || prop.getProperty(KEY_EMUI_VERSION, null) != null
                        || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                    SYS = SYS_EMUI
                } else if (meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
                    SYS = SYS_FLYME
                } else if (Build.MANUFACTURER.equals("oppo", ignoreCase = true)) {
                    SYS = SYS_OPPO
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return SYS
            }

            return SYS
        }

    val meizuFlymeOSFlag: String
        get() = getSystemProperty("ro.build.display.id", "")

    private fun getSystemProperty(key: String, defaultValue: String): String {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val get = clz.getMethod("get", String::class.java, String::class.java)
            return get.invoke(clz, key, defaultValue) as String
        } catch (e: Exception) {
        }

        return defaultValue
    }
}
