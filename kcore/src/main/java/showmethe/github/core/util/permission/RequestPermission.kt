package showmethe.github.core.util.permission

/**
 * showmethe.github.core.util.permission
 * 2019/10/29
 **/

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class  RequestPermission(val permissions:Array<String>)