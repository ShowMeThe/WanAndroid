package showmethe.github.core.glide

import android.graphics.drawable.Drawable

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory

/**
 * showmethe.github.core.glide
 * 揭露动画+Scale动画切换
 * 2019/10/11
 */
class DrawableScaleFadeFactory (
    private val duration: Int,
    private val isCrossFadeEnabled: Boolean,
    private val isReveal:Boolean = false
) : TransitionFactory<Drawable> {
    private var resourceTransition: DrawableScaleFadeTransition? = null

    override fun build(dataSource: DataSource, isFirstResource: Boolean): Transition<Drawable> {
        return if (dataSource == DataSource.MEMORY_CACHE)
            NoTransition.get()
        else
            getResourceTransition()
    }


    private fun getResourceTransition(): Transition<Drawable> {
        if (resourceTransition == null) {
            resourceTransition = DrawableScaleFadeTransition(duration, isCrossFadeEnabled,isReveal)
        }
        return resourceTransition!!
    }


    class Builder
    /** @param durationMillis The duration of the cross fade animation in milliseconds.
     */
    @JvmOverloads constructor(private val durationMillis: Int = DEFAULT_DURATION_MS) {
        private var isCrossFadeEnabled: Boolean = false


        fun setCrossFadeEnabled(isCrossFadeEnabled: Boolean): DrawableScaleFadeFactory.Builder {
            this.isCrossFadeEnabled = isCrossFadeEnabled
            return this
        }

        fun build(): DrawableScaleFadeFactory {
            return DrawableScaleFadeFactory(durationMillis, isCrossFadeEnabled)
        }

        companion object {
            private const val DEFAULT_DURATION_MS = 300
        }
    }
}
