package showmethe.github.core.livebus

/**
 * PackageName: example.ken.com.library.livebus
 * Author : jiaqi Ye
 * Date : 2018/9/26
 * Time : 9:20
 */
class LiveBusHelper {
    var code: Int = 0

    var data: Any? = null

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: Any) {
        this.code = code
        this.data = data
    }
}
