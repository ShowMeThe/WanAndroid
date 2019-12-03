package showmethe.github.core.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

/**
 * showmethe.github.core.base
 * 2019/12/3
 **/
class ContextProvider private constructor(){


    lateinit var context: Context
    private  var ctx: WeakReference<AppCompatActivity>? = null

    companion object{

        private val instant by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ ContextProvider() }

        fun get() = instant
    }


    fun attach(context: Context){
        this.context = context
    }

    fun attach(activity: AppCompatActivity){
        ctx = WeakReference(activity)
    }

    fun getActivity() = ctx!!.get()

}