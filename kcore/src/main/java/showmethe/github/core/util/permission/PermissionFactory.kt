package showmethe.github.core.util.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import showmethe.github.core.base.vmpath.VMPath
import java.lang.ref.WeakReference

import java.util.ArrayList
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

/**
 * Author: showMeThe
 * Update Time: 2019/10/29 16:16
 * Package Name:showmethe.github.core.util.permission
 */
class PermissionFactory : DefaultLifecycleObserver{

    companion object{

        private var weakReference : WeakReference<FragmentActivity>? = null
        private var instant : PermissionFactory? = null
        fun with(activity: FragmentActivity) : PermissionFactory{
            weakReference = WeakReference(activity)
            if(instant == null){
                instant =   PermissionFactory()
            }
            return instant!!
        }
    }

    private var hasAdd = false
    private val requestPermission = ArrayList<String>()
    private var  targetCall : KCallable<*>? = null
    private var fragment: PermissionFragment? = null

    fun requestAll(){
        weakReference?.get()?.apply  {
            lifecycle.addObserver(this@PermissionFactory)
            kotlin.run breaking@{
                this::class.members.forEach { call ->
                    val annotation = call.findAnnotation<RequestPermission>()
                    annotation?.apply {
                        this.permissions.forEach {
                            targetCall = call
                            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                                requestPermission.add(it)
                            }
                        }
                        if (requestPermission.isNotEmpty()) {
                            invoke(requestPermission)
                        }else{
                            invokeResult(true)
                        }
                        return@breaking
                    }
                }
            }
        }
    }


    private fun invoke(permissions : ArrayList<String>){
        weakReference?.get()?.apply {
            fragment = PermissionFragment.get(permissions)
            supportFragmentManager.beginTransaction().add(fragment!!,fragment!!::class.java.name).commitNow()
            hasAdd = true
            fragment?.apply {
                fragment?.setOnCallPermissionResult {
                    invokeResult(it)
                }
            }

        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        requestPermission.clear()
        targetCall = null
        if(hasAdd){
            weakReference?.get()?.supportFragmentManager?.beginTransaction()?.remove(fragment!!)
        }
        weakReference?.get()?.lifecycle?.removeObserver(this)
        weakReference = null
    }

    private fun invokeResult(boolean: Boolean){
        weakReference?.get()?.apply {
            targetCall?.isAccessible = true
            targetCall?.call(this@apply,boolean)
        }

    }

}
