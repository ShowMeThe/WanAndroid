package showmethe.github.core.glide

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class ProgressInterceptor : Interceptor {

    val LISTENER_MAP: WeakHashMap<String, ProgressListener> = WeakHashMap()

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

}