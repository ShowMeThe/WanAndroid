package showmethe.github.core.util.system.crash

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.base.ContextProvider

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap
import kotlin.system.exitProcess

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.system.crash
 */
/**
 * 捕捉异常保存到本地
 */
class CrashHandler /** 保证只有一个CrashHandler实例  */
private constructor() : Thread.UncaughtExceptionHandler {


    //收集错误次数
    private var repeatTime = 0



    // 系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null
    private var mContext: Context? = null


    // 用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()



    private val globalpath: String
        get() = (mContext!!.externalCacheDir!!.absolutePath
                + File.separator + "crash" + File.separator)

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context) {
        mContext = context
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }



    private fun getActivityPair(): Pair<Activity?, Intent?> {
        val activity = ContextProvider.get().getActivity()
        val intent = if (activity?.intent?.action == "android.intent.action.MAIN")
            Intent(activity, activity.javaClass)
        else
            activity?.intent

        intent?.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        return Pair(activity, intent)
    }

    /**
     * 通过包名拉起app
     */
    private fun restartApp(activityPair: Pair<Activity?, Intent?>) {
        val packageName = activityPair.first?.baseContext?.packageName
        if (packageName != null) {
            val intent = activityPair.first?.baseContext?.packageManager?.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activityPair.first?.startActivity(intent)
            }
            activityPair.first?.overridePendingTransition(0, 0)
            activityPair.first?.finish()
            activityPair.first?.overridePendingTransition(0, 0)
        }
        repeatTime = 0
    }


    /**
     * 重启activity 拉起的activity 不能为singleTask
     */
    private fun restartActivity(activityPair: Pair<Activity?, Intent?>){
        val restartClass = guessRestartActivityClass()
        val intent = Intent(activityPair.first,restartClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityPair.first?.startActivity(intent)
        activityPair.first?.overridePendingTransition(0, 0)
        activityPair.first?.finish()
        activityPair.first?.overridePendingTransition(0, 0)
    }


    private fun guessRestartActivityClass(): Class<out Activity>? {
        return getRestartActivityClassWithIntentFilter()
    }


    /**
     * 通过filter从包里拉起actitvity
     */
    private  fun  getRestartActivityClassWithIntentFilter() : Class<out Activity>?{
        val searchedIntent = Intent().setAction(restartAction).setPackage(ContextProvider.get().context.packageName)
        val resolveInfos = ContextProvider.get().context.packageManager.queryIntentActivities(searchedIntent,
            PackageManager.MATCH_DEFAULT_ONLY)
        if (resolveInfos != null && resolveInfos.size > 0) {
            val resolveInfo = resolveInfos[0]
            try {
                return Class.forName(resolveInfo.activityInfo.name) as Class<out Activity>
            } catch (e: ClassNotFoundException) {
                Log.e(TAG,
                    "Failed when resolving the restart activity class via intent filter, stack trace follows!", e)
            }

        }
        return null
    }


    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            val activityPair = getActivityPair()
            restartActivity(activityPair)
            // 退出当前程序
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null)
            return false

        try {
            // 收集设备参数信息
            collectDeviceInfo(mContext)
            // 保存日志文件
            saveCrashInfoFile(ex)
            Log.e(TAG,ex.message)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    @RequiresApi(Build.VERSION_CODES.P)
    fun collectDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx!!.packageManager
            val pi = pm.getPackageInfo(ctx.packageName,
                    PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = pi.versionName + ""
                val versionCode = pi.longVersionCode.toString() + ""
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
                infos["android version"] = Build.VERSION.RELEASE
                infos["phoneType"] = Build.BRAND + "  " + Build.MODEL
            }
        } catch (e: Exception) {
            Log.e(TAG, "an error occured when collect package info", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = field.get(null).toString()
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }

        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun saveCrashInfoFile(ex: Throwable): String? {
        val sb = StringBuffer()
        try {
            val sDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT)
            val date = sDateFormat.format(Date())
            sb.append("\r\n" + date + "\n")
            for ((key, value) in infos) {
                sb.append("$key=$value\n")
            }

            val writer = StringWriter()
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            var cause: Throwable? = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.flush()
            printWriter.close()
            val result = writer.toString()
            sb.append(result)

            return writeFile(sb.toString())
        } catch (e: Exception) {
            writeFile(sb.toString())
        }

        return null
    }

    @Throws(Exception::class)
    private fun writeFile(sb: String): String {
        val time = System.currentTimeMillis().toString() + ""
        val fileName = "crash-$time.txt"
        try {
            val path = globalpath
            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()
            val fos = FileOutputStream(path + fileName, true)
            fos.write(sb.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return fileName
    }

    companion object {
        var TAG = "Crash"

        private var restartAction = "com.crash.restart"
        private val instant by lazy { CrashHandler() }

        fun get(ctx: Context): CrashHandler {
            instant.init(ctx)
            return instant
        }
    }

}
