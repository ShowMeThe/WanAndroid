package showmethe.github.core.glide

import android.util.Log

import java.io.IOException

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

class ProgressResponseBody(var url: String, private val responseBody: ResponseBody) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    private var listener: ProgressListener? = null

    init {
        listener = TGlide.interceptor.LISTENER_MAP[url]
    }

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = ProgressSource(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private inner class ProgressSource internal constructor(source: Source) : ForwardingSource(source) {

        internal var totalBytesRead  = 0f

        internal var currentProgress  = 0f

        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength = responseBody.contentLength()
            if (bytesRead == -1L) {
                totalBytesRead = fullLength.toFloat()
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength)

            if (listener != null && progress != currentProgress) {
                listener!!.onProgress(progress)
            }
            if (listener != null && totalBytesRead == fullLength.toFloat()) {
                TGlide.interceptor.removeListener(url)
                listener = null
            }
            currentProgress = progress
            return bytesRead
        }
    }

    companion object {

        private val TAG = "ProgressResponseBody"
    }

}