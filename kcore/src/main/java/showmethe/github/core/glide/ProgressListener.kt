package showmethe.github.core.glide

@FunctionalInterface
interface ProgressListener {
    fun onProgress(progress: Float)
}
