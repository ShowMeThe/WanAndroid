package showmethe.github.core.glide

import java.io.IOException
import java.util.HashMap

import okhttp3.Interceptor
import okhttp3.Response

class ProgressInterceptor : Interceptor {

    val LISTENER_MAP: MutableMap<String, ProgressListener> = HashMap()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val url = request.url.toString()
        val body = response.body
        return response.newBuilder().body(ProgressResponseBody(url, body!!)).build()
    }

    fun addListener(url: String, listener: ProgressListener) {
        LISTENER_MAP[url] = listener
    }

    fun removeListener(url: String) {
        LISTENER_MAP.remove(url)
    }

    companion object {



        private val instant by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ ProgressInterceptor() }

        fun get() : ProgressInterceptor = instant


    }
}