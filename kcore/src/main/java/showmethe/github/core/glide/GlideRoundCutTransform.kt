package showmethe.github.core.glide

import android.content.res.Resources
import android.graphics.*

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

import java.security.MessageDigest
import kotlin.math.sqrt

/**
 * PackageName: com.library.utils.glide
 * Author : jiaqi Ye
 * Date : 2018/7/2
 * Time : 16:52
 */
class GlideRoundCutTransform @JvmOverloads constructor(var radius  : Float = 4f)  : BitmapTransformation() {


    init {
        radius *= Resources.getSystem().displayMetrics.density
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return roundCrop(pool, toTransform)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null


        var   result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
          if (result == null) {
              result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
          }
        val canvas = Canvas(result!!)
        val paint = Paint()
        val path = Path()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        var cut = (sqrt(2.0) /2 * radius).toFloat()
        val width: Float = source.width.toFloat()
        val height: Float = source.height.toFloat()

        if(width/height>=1){
            if(cut >height/2){
                cut = height/2
            }
        }else {
            if(cut >width/2){
                cut = width/2
            }
        }

        path.moveTo(cut,0f)
        path.lineTo(width-cut,0f)
        path.lineTo(width,cut)
        path.lineTo(source.width.toFloat(),height-cut)
        path.lineTo(width-cut,height)
        path.lineTo(cut,height)
        path.lineTo(0f,height-cut)
        path.lineTo(0f,cut)
        path.lineTo(cut,0f)
        canvas.drawPath(path, paint)
        return result
    }
}
