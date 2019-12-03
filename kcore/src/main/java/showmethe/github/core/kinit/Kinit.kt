package showmethe.github.core.kinit

import android.util.ArrayMap
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Author: showMeThe
 * Update Time: 2019/12/3
 * Package Name:showmethe.github.core.kinit
 */



fun startInit(component: Component.()->Unit){
    component.invoke(Component.get())
}


class Component {

    companion object{

        private val entry = ArrayMap<String,Lazy<Any?>>()

        private val instant by lazy { Component() }

        fun get() = instant

        fun getEntry() = entry
    }



    fun modules(vararg module: Module<*>){
        module.forEach {
            inject(it)
        }
    }


    private fun inject(module: Module<*>){
        entry[module.moduleName()] = module.injectModule()
    }

}

inline fun <reified T> get() : T{
   return Component.getEntry()[T::class.java.name]!!.value as T
}

inline fun <reified T> inject() : Lazy<T> {
    return Component.getEntry()[T::class.java.name] as Lazy<T>
}

abstract class Module<T>{


     open fun moduleName() = ""

     abstract fun injectModule() : Lazy<T>
}

inline fun<reified T> getClassName() : String = T::class.java.name


