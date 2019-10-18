package showmethe.github.core.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import showmethe.github.core.R
import java.lang.Exception

abstract  class SimpleBSheetDialogFragment  : BottomSheetDialogFragment() , DefaultLifecycleObserver {

    var mBehavior: BottomSheetBehavior<*>? = null

    private lateinit var activity : Context

    private var onCreate :(()->Int)? = null

    private var onWindow :((window: Window)->Unit)? = null

    private var onView :((view: View)->Unit)? = null

    abstract fun build(savedInstanceState: Bundle?)

    private var owner : LifecycleOwner? = null

    private fun init(owner: LifecycleOwner){
        this.owner = owner
        this.owner?.lifecycle?.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        dialog.apply {
            dismissAllowingStateLoss()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if(this.owner != null){
            dismissAllowingStateLoss()
            this.owner?.lifecycle?.removeObserver(this)
        }
    }

    fun buildDialog(onCreate :(()->Int)) : SimpleBSheetDialogFragment{
        this.onCreate = onCreate
        return this
    }


    fun onWindow(onWindow :((window: Window)->Unit)) : SimpleBSheetDialogFragment{
        this.onWindow = onWindow
        return this
    }

    fun <T : ViewDataBinding> View.onBindingView(onBindingView :((binding : T?)->Unit)){
        onBindingView.invoke(DataBindingUtil.bind<T>(this))
    }


    fun onView(onView :((view: View)->Unit)) : SimpleBSheetDialogFragment{
        this.onView = onView
        return this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context
        if(context is AppCompatActivity){
            init(context)
        }else if(context is LifecycleOwner){
            init(context)
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        build(savedInstanceState)
        val viewId = onCreate?.invoke()
        if(viewId!= null){

            val param = javaClass.getAnnotation(WindowParam::class.java)!!
            val gravity  = param.gravity
            val outSideCanceled   = param.outSideCanceled
            val canceled  = param.canceled
            val dimAmount = param.dimAmount
            val noAnim  = param.noAnim

            val view = View.inflate(activity, viewId, null)
            val dialog = BottomSheetDialog(context!!, R.style.FullScreenBottomSheet)
            dialog.setContentView(view)
            dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
            mBehavior = BottomSheetBehavior.from<View>(view.parent as View)


            dialog.setCanceledOnTouchOutside(outSideCanceled)
            dialog.setCancelable(canceled)

            val window = dialog.window
            val dm = DisplayMetrics()
            window?.apply {
                windowManager.defaultDisplay.getMetrics(dm)
                setLayout(dm.widthPixels, window.attributes.height)
                setBackgroundDrawable(ColorDrawable(0x00000000))
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                setGravity(gravity)
                if(!noAnim){
                    setWindowAnimations(R.style.AnimBottom)
                }
                if(dimAmount!=-1f){
                    setDimAmount(dimAmount)
                }
                onWindow?.invoke(this)
            }
            view?.apply {
                onView?.invoke(this)
            }

            return dialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun dismiss() {
        dialog?.apply {
            if(isShowing){
                if(getActivity()!=null){
                    super.dismiss()
                }
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if(!isAdded){
                val transaction = manager.beginTransaction()
                transaction.add(this, tag)
                transaction.commitAllowingStateLoss()
                transaction.show(this)
            }
        }catch (e: Exception){}
    }

}