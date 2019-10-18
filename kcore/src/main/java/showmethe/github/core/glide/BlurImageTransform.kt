package showmethe.github.core.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.commit451.nativestackblur.NativeStackBlur

import java.security.MessageDigest

/**
 * PackageName: com.library.utils.glide
 * Author : jiaqi Ye
 * Date : 2018/7/3
 * Time : 16:32
 */
class BlurImageTransform constructor(var radius: Int) : BitmapTransformation() {

    constructor() : this(15)

    companion object {
        private var blurRadius: Int = 0
    }


    init {
        blurRadius = radius
    }


    override fun transform(pool: BitmapPool, source: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {

        var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(source, 0f, 0f, paint)
        result = NativeStackBlur.process(result, blurRadius)

        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }


}
