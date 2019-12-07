package showmethe.github.core.util.extras

import androidx.databinding.ObservableArrayList

/**
 * Author: showMeThe
 * Update Time: 2019/11/7 10:36
 * Package Name:showmethe.github.core.util.extras
 */

typealias   obArrayList<T> = ObservableArrayList<T>


inline fun <T> Iterable<T>.forEachBreak(action: (T) -> Boolean ){
    kotlin.run breaking@{
        for (element in this)
            if(!action(element)){
                return@breaking
            }
    }
}

inline fun <T> Collection<T>.forEachBreak(action: (T) -> Boolean ){
    kotlin.run breaking@{
        for (element in this)
            if(!action(element)){
                return@breaking
            }
    }
}

inline fun <T> Array<out T>.forEachBreak(action: (T) -> Boolean ){
    kotlin.run breaking@{
        for (element in this)
            if(!action(element)){
                return@breaking
            }
    }
}