package showmethe.github.core.util.permission

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.ArrayList

/**
 * showmethe.github.core.util.permission
 * 2019/10/29
 **/
class PermissionFragment : Fragment() {

    companion object{

        private const val PERMISSION_REQUEST_CODE = 502
        fun get(permissions : ArrayList<String>) : PermissionFragment{
            val fragment = PermissionFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("permissions",permissions)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val permissions =  ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.apply {
            permissions.addAll(getStringArrayList("permissions"))
        }
        if(permissions.isNotEmpty()){
            requestPermissions(permissions.toTypedArray(),PERMISSION_REQUEST_CODE)
        }else{
            onCallPermission?.invoke(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultCallBack(activity,requestCode,permissions,grantResults)
    }

   private  fun onRequestPermissionsResultCallBack(
        activity: FragmentActivity?,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        var allow = true
         if (requestCode == PERMISSION_REQUEST_CODE) {
          for (permission in permissions) {
              if (!activity!!.shouldShowRequestPermissionRationale(permission)) {
                  for (i in grantResults.indices) {
                      allow = grantResults[i] == 0
                  }
              }
          }
      }
       onCallPermission?.invoke(allow)
    }

    private var onCallPermission : ((result:Boolean)->Unit)? = null
    fun setOnCallPermissionResult(onCallPermission : ((result:Boolean)->Unit)){
        this.onCallPermission = onCallPermission
    }

    override fun onDestroy() {
        super.onDestroy()
        permissions.clear()
    }

}