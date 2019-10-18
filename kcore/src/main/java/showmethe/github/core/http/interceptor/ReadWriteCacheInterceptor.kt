package showmethe.github.core.http.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import showmethe.github.core.base.BaseApplication.Companion.context
import showmethe.github.core.util.system.Network
import showmethe.github.core.util.system.checkConnection
import java.io.IOException

/**
 * example.ken.com.library.http
 * YeJq
 * 2018/12/4
 */
class ReadWriteCacheInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (checkConnection(context) && Network.get().networkState) {
            val response = chain.proceed(request)
            // read from cache for 30 s  有网络不会使用缓存数据
            val maxAge = 0
            val cacheControl = request.cacheControl.toString()
            return response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .build()
        } else {
            //无网络时强制使用缓存数据
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            val response = chain.proceed(request)
            //            int maxStale = 60;
            val maxStale = 60 * 60 * 24 * 3
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .build()
        }
    }
}
