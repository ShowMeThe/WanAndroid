package showmethe.github.core.base.vmpath

import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import showmethe.github.core.util.extras.forEachBreak
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation


class VMRouter(private var viewModel: ViewModel) {

    companion object{

        val  callMap = ArrayMap<String,KCallable<*>>()

        fun init(viewModel: ViewModel) : VMRouter{
            return VMRouter(viewModel)
        }
    }

    fun toTarget(path: String,vararg args: Any?){

        if(callMap["${viewModel.javaClass.name}/${path}"] == null){
            viewModel.viewModelScope.launch(Dispatchers.Main) {
                try {
                    viewModel::class.members.forEachBreak { call->
                        val annotation = call.findAnnotation<VMPath>()
                        annotation?.apply {
                            if(this.path == path){
                                if(args.isNotEmpty()){
                                    call.call(viewModel,*args)
                                }else{
                                    call.call(viewModel)
                                }
                                callMap["${viewModel.javaClass.name}/${path}"] = call
                                return@forEachBreak false
                            }
                        }
                        true
                    }
                }catch (e:Exception){
                    Log.e("ViewModel","Exception  : ${e.message}")
                }
            }
        }else{
            try {
                callMap["${viewModel.javaClass.name}/${path}"]?.apply {
                    if(args.isNotEmpty()){
                        call(viewModel,*args)
                    }else{
                       call(viewModel)
                    }
                }
            }catch (e:Exception){
                Log.e("ViewModel","Exception  call : ${e.message}")
            }
        }


    }
}