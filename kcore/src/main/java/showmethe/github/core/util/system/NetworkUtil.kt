package showmethe.github.core.util.system

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.system
 */

/**
 * 网络连接状态
 * @param context
 * @return
 */
fun checkConnection(context: Context): Boolean {
   return try {
      val cm = context
         .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val networkInfo = cm.activeNetworkInfo
      !(networkInfo == null || !networkInfo.isConnected)
   }catch (e:Exception){
       false
   }
}

fun startLocalForIp(context: Context){
    val constraints = Constraints.Builder()
        .setRequiresBatteryNotLow(true) // 非电池低电量
        .setRequiredNetworkType(NetworkType.CONNECTED) // 网络连接的情况
        .setRequiresCharging(true) //充电时
        .setRequiresStorageNotLow(true) // 存储空间足
        .build()
    val workBuilder = PeriodicWorkRequest.Builder(NetworkWork::class.java,15L,TimeUnit.MINUTES,5,TimeUnit.SECONDS)
        .setConstraints(constraints)
        .build()
    WorkManager.getInstance(context).enqueue(workBuilder)
}

class  NetworkWork(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        GlobalScope.launch(Dispatchers.IO){
            pingIP(this.coroutineContext,"www.wanandroid.com")
        }
        return Result.success()
    }
}

class Network{
    var networkState = true
    private var requestTime = 0
    companion object{
        private val instant : Network by lazy { Network() }
        fun get() = instant
    }

    fun addTime(){
        startCheck()
        requestTime ++
    }

    private fun startCheck(){
       if(requestTime%15 == 0){
           GlobalScope.launch(Dispatchers.IO){
               pingIP(this.coroutineContext,"www.wanandroid.com")
           }
       }
    }
}

 suspend fun pingIP(context: CoroutineContext,address:String) : Boolean{
    return withContext(context){
         try {
             val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 $address")
             val status = process.waitFor()
             Network.get().networkState = status == 0
             status == 0
         }catch (e : Exception){
             Network.get().networkState = false
             false
         }
     }
}


