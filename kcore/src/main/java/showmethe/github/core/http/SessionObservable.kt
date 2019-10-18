package showmethe.github.core.http

/**
 * showmethe.github.core.http
 * cuvsu
 * 2019/4/2
 */
interface SessionObservable {
    fun notify(session: String) //通知Session变化
}
