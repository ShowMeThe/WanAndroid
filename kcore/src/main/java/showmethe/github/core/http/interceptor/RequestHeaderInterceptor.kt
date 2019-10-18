package showmethe.github.core.http.interceptor


import java.io.IOException

import okhttp3.Interceptor
import showmethe.github.core.http.SessionObserver
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.system.Network

class RequestHeaderInterceptor : Interceptor, SessionObserver {

    override fun update(sessionId: String) {
        this.sessionId = sessionId
    }


    var sessionId = ""
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val originalHttpUrl = request.url
        val htbuilder = originalHttpUrl.newBuilder()


        val url = htbuilder.build()
        val builder = request.newBuilder().url(url)

        Network.get().addTime()
        if(this.sessionId.isEmpty()){
            this.sessionId =  RDEN.get("sessionId","")
        }
        builder.addHeader("Cookie", "JSESSIONID=$sessionId")
        builder.addHeader("Content-Type","application/json")
        builder.addHeader("Accept","application/json")
        return chain.proceed(builder.build())
    }
}