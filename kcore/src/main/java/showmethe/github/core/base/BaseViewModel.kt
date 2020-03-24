package showmethe.github.core.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import showmethe.github.core.base.vmpath.VMRouter
import showmethe.github.core.util.toast.ToastFactory
import java.lang.Exception

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application),DefaultLifecycleObserver{


    lateinit var owner: LifecycleOwner


    fun initOwner(vmRouter: VMRouter){
            vmRouter.owner?.apply {
                owner = this
            }
        val fields = this::class.java.declaredFields
        for(field in fields){
            if(field.isAnnotationPresent(InjectOwner::class.java)){
                field.isAccessible = true
               try {
                   val member = field.get(this)
                   if(member is BaseRepository){
                       member.init(vmRouter)
                   }
               }catch (e:Exception){
                   continue
               }
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner

        onViewModelCreated(owner)

    }

    override fun onResume(owner: LifecycleOwner) {


    }

    abstract fun onViewModelCreated(owner: LifecycleOwner)



    fun showToast(message : String){
        ToastFactory.createToast(message)
    }


}

