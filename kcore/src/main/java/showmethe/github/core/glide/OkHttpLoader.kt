package showmethe.github.core.glide

/**
 * showmethe.github.kframework.glide
 * 2019/10/28
 */

import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory

import java.io.InputStream

import okhttp3.Call
import okhttp3.OkHttpClient

/**
 * A simple model loader for fetching media over http/https using OkHttp.
 */
class OkHttpLoader// Public API.
(private val client: Call.Factory) : ModelLoader<GlideUrl, InputStream> {

    override fun handles(url: GlideUrl): Boolean {
        return true
    }

    override fun buildLoadData(model: GlideUrl, width: Int, height: Int,
                               options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(model, OkHttpStreamFetcher(client, model))
    }

    /**
     * The default factory for [OkHttpLoader]s.
     */
    // Public API.
    class Factory
    /**
     * Constructor for a new Factory that runs requests using given client.
     *
     * @param client this is typically an instance of `OkHttpClient`.
     */
    @JvmOverloads constructor(private val client: Call.Factory? = getInternalClient()) : ModelLoaderFactory<GlideUrl, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return OkHttpLoader(client!!)
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }

        companion object {
            @Volatile
            private var internalClient: Call.Factory? = null

            private fun getInternalClient(): Call.Factory? {
                if (internalClient == null) {
                    synchronized(Factory::class.java) {
                        if (internalClient == null) {
                            internalClient = OkHttpClient()
                        }
                    }
                }
                return internalClient
            }
        }
    }
    /**
     * Constructor for a new Factory that runs requests using a static singleton client.
     */
}