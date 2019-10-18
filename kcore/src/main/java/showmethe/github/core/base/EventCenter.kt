package showmethe.github.core.base

import android.util.SparseArray

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
class EventCenter {
    var code: Int = 0
    var data: Any? = null
    var parmas: SparseArray<Any>? = null

    constructor(code: Int, parmas: SparseArray<Any>) {
        this.code = code
        this.parmas = parmas
    }

    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, data: Any) {
        this.code = code
        this.data = data
    }
}
