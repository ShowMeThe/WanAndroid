package showmethe.github.core.base

import android.annotation.SuppressLint
import android.content.Context

/**
 * showmethe.github.core.base
 * 2019/12/3
 **/
class ContextProvider private constructor(){


    lateinit var context: Context

    companion object{

        private val instant by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ ContextProvider() }

        fun get() = instant
    }


    fun attach(context: Context){
        this.context = context
    }

}