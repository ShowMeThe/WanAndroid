package showmethe.github.core.http.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import retrofit2.Response
import showmethe.github.core.http.JsonResult

import showmethe.github.core.http.fromJson

/**
 * Author: showMeThe
 * Update Time: 2019/10/16
 * Package Name:showmethe.github.core.http.coroutines
 */
class CallResult<T> constructor(private var owner: LifecycleOwner?,callResult: CallResult<T>.()->Unit) {

    init {
        callResult()
    }

    fun hold(result: suspend () -> Response<JsonResult<T>>){
        var response: Response<JsonResult<T>>?
        var netJob: Job? = null
        owner?.apply {
            netJob = lifecycleScope.launchWhenStarted {
                withContext(Dispatchers.Main) {
                    onLoading?.invoke()
                }
                response  = withContext(Dispatchers.IO) {
                    withTimeoutOrNull(10000){
                        result.invoke()
                    }
                }
                withContext(Dispatchers.Main) {
                    if (response != null) {
                        response?.run {
                            if(code() != 200){
                                loadingOutTime?.invoke(Result(Result.OutTime))
                                netJob?.cancel()
                            }else{
                                build(response)
                            }
                        }
                    } else {
                        loadingOutTime?.invoke(Result(Result.OutTime))
                        netJob?.cancel()
                    }
                }
            }
        }
    }

    private fun build(response: Response<JsonResult<T>>?) {
        response?.apply {
            if (!isSuccessful) {
                try {
                    val result = errorBody().toString().fromJson<JsonResult<T>>()
                    if (result != null) {
                        val errorMessage = result.errorMsg!!
                        onError?.invoke(Result(Result.Failure, null, -1, errorMessage), -1, errorMessage)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    onError?.invoke(Result(Result.Failure, null, -1, e.message.toString()), -1, e.message.toString())
                }
            } else {
                try {
                    if (body() == null) {
                        onError?.invoke(Result(Result.Failure, null, -1, ""), -1, "")
                    } else {
                        if (body()?.errorCode == 0) { // 正确errorCode 返回成功
                            onSuccess?.invoke(
                                Result(Result.Success, body()?.data, body()?.errorCode!!, body()?.errorMsg!!),
                                body()?.errorMsg!!
                            )
                        } else {
                            onError?.invoke(
                                Result(Result.Failure, null, body()?.errorCode!!, body()?.errorMsg!!),
                                body()?.errorCode!!,
                                body()?.errorMsg!!
                            )
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private var onLoading: (() -> Unit)? = null

    fun loading(onLoading: (() -> Unit)): CallResult<T> {
        this.onLoading = onLoading
        return this
    }

    private var loadingOutTime: ((result: Result<T>) -> Unit)? = null
    fun outTime(loadingOutTime: ((result: Result<T>) -> Unit)) : CallResult<T>{
        this.loadingOutTime = loadingOutTime
        return this
    }


    private var onSuccess: ((result: Result<T>, message: String) -> Unit)? = null

    private var onError: ((result: Result<T>, code: Int, message: String) -> Unit)? = null

    fun success(onSuccess: ((result: Result<T>, message: String) -> Unit)): CallResult<T> {
        this.onSuccess = onSuccess
        return this
    }


    fun error(onError: ((result: Result<T>, code: Int, message: String) -> Unit)): CallResult<T> {
        this.onError = onError
        return this
    }


}