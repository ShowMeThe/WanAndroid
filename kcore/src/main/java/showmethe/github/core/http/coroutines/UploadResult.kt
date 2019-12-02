package showmethe.github.core.http.coroutines

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import showmethe.github.core.http.JsonResult

import showmethe.github.core.http.fromJson
import java.io.File


class UploadResult<T> constructor(private var owner: LifecycleOwner?,private var file: File,private var requestParam: String) {

    fun hold(result: suspend (body: MultipartBody.Part) -> Response<JsonResult<T>>): UploadResult<T> {
        var response: Response<JsonResult<T>>?
        var netJob: Job? = null
        val requestBody  = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(requestParam, file.name, requestBody)
        owner?.apply {
            netJob = lifecycleScope.launchWhenStarted {
                response  = withContext(Dispatchers.IO) {
                    result.invoke(body)
                }
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    withContext(Dispatchers.Main) {
                        if (response != null) {
                            response?.run {
                                if (code() != 200) {
                                    onError?.invoke(Result(Result.Failure),code(),message())
                                } else {
                                    build(response)
                                }
                            }
                        }
                    }
                } else {
                    netJob?.cancel()
                }
            }
        }
        return this
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

    private var onSuccess: ((result: Result<T>, message: String) -> Unit)? = null

    private var onError: ((result: Result<T>, code: Int, message: String) -> Unit)? = null

    fun success(onSuccess: ((result: Result<T>, message: String) -> Unit)): UploadResult<T> {
        this.onSuccess = onSuccess
        return this
    }

    fun error(onError: ((result: Result<T>, code: Int, message: String) -> Unit)): UploadResult<T> {
        this.onError = onError
        return this
    }
}
