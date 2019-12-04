package showmethe.github.core.kinit

import android.util.ArrayMap
import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Author: showMeThe
 * Update Time: 2019/12/3
 * Package Name:showmethe.github.core.kinit
 */


fun startInit(component: Components.()->Unit){
    component.invoke(Components.get())
}

class Components {

    companion object{

        private val entry = ArrayMap<String,Any?>()
        private val module = ArrayList<Module>()

        private val instant by lazy { Components() }

        fun get() = instant

        fun getEntry() = entry
    }

    fun modules(vararg modules: Module){
        module.addAll(modules)
    }

}

inline fun <reified T> get(name: String = T::class.java.name) : T{
   return Components.getEntry()[name] as T
}

inline fun <reified T> inject(name: String = T::class.java.name) : Lazy<T> {

    return lazy { Components.getEntry()[name]  as T }
}


class Module(component: Component.() -> Unit){

    init {
        component.invoke(Component(""))
    }
}

class Component(var name:String){

    inline fun <reified T>single(noinline single: Component.()->T){
        name = T::class.java.name
        Components.getEntry()[name] = single()
    }
}




