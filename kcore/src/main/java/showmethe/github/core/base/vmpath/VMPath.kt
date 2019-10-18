package showmethe.github.core.base.vmpath


@Target(AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class VMPath(val path:String)
