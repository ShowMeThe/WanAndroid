package showmethe.github.core.http.coroutines

import android.util.ArrayMap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import okhttp3.MultipartBody
import retrofit2.Response
import showmethe.github.core.http.JsonResult
import java.io.File


fun <T>uploadFile(owner: LifecycleOwner,file:File,requestParam: String,
                  call: suspend (body: MultipartBody.Part) -> Response<JsonResult<T>>,
                  onSuccess: ((result: Result<T>, message: String) -> Unit)? = null,
                  onError: ((result: Result<T>, code: Int, message: String) -> Unit)? = null){
    UploadResult<T>(owner,file,requestParam)
        .success { result, message ->
            onSuccess?.invoke(result,message)
        }.error { result, code, message ->
            onError?.invoke(result, code, message)
        }.hold {
            call.invoke(it)
        }
}


/**
 * 批量上传
 */
fun <T>uploadFiles(owner: LifecycleOwner,files:ArrayList<File>,requestParam: String,
                   call: suspend (body: MultipartBody.Part) -> Response<JsonResult<T>>,
                   onProcess : ((result: T?) -> String),
                   onComplete: ((result:ArrayList<String>,successCount:Int,failedCount:Int) -> Unit),
                   onError: ((code: Int, message: String) -> Unit)? = null){

    val sourceSize = files.size
    val completeList = ArrayMap<Int,String>()
    val completeCount = MutableLiveData<Int>()
    var successCount = 0
    var failedCount = 0

    completeCount.value = 0
    completeCount.observe(owner, Observer {
        it?.apply {
            if(this == sourceSize){
                onComplete.invoke(ArrayList(completeList.values),successCount, failedCount)
            }
        }
    })

    for((index,file) in files.withIndex()){
        UploadResult<T>(owner, file, requestParam)
            .success { result, message ->
                result.apply {
                    completeList[index] = onProcess.invoke(response)
                }
                completeCount.value = completeCount.value!! + 1
                successCount = successCount.inc()
            }.error { result, code, message ->
                onError?.invoke(code, message)
                completeCount.value = completeCount.value!! + 1
                failedCount = failedCount.inc()
            }.hold {
                call.invoke(it)
            }
    }
}