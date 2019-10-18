package showmethe.github.core.http

/**
 * showmethe.github.core.http
 * cuvsu
 * 2019/3/6
 **/
data class Resource<T> (val status: Int, val data: T?,val code : Int ,val message: String) {

    companion object {
        const val SUCCESS = 1
        const val ERROR = 2
        const val LOADING = 3
    }

}