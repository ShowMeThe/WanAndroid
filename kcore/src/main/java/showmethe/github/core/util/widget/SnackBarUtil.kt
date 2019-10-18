package showmethe.github.core.util.widget

import android.app.Activity
import android.graphics.Color
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.SnackbarContentLayout

import java.lang.ref.SoftReference

import showmethe.github.core.R


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.widget
 */

class SnackBarUtil(activity: Activity) {

    private val reference: SoftReference<Activity> = SoftReference(activity)

    private var parent: SoftReference<Snackbar.SnackbarLayout>? = null
    var snackbar: Snackbar? = null
    private var textView: SoftReference<TextView>? = null
    lateinit var  view : View

    init {
        make()
    }

    private fun make() {
        if(reference.get()== null){
            return
        }
        reference.get()?.apply {
            val view = findViewById<View>(android.R.id.content)
            snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        }
        parent = SoftReference(snackbar!!.view as Snackbar.SnackbarLayout)
        parent?.apply {
            get()?.apply {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                setBackgroundColor(Color.TRANSPARENT)
                setPadding(0, 0, 0, 0)
            }
        }

        val contentView = parent!!.get()!!.getChildAt(0) as SnackbarContentLayout
        val newContentView = LayoutInflater.from(view.context).inflate(R.layout.snack_base, contentView, false) as SnackbarContentLayout
        newContentView.id = contentView.id

        textView = SoftReference(newContentView.findViewById(R.id.text))
        textView!!.get()?.apply {
            id = com.google.android.material.R.id.snackbar_text
        }
        snackbar!!.view.setPadding(0, 0, 0, 0)
        try {
            val method = newContentView.javaClass.getDeclaredMethod("onFinishInflate")
            method.isAccessible = true
            method.invoke(newContentView)
            textView!!.get()!!.text = ""
            val index = parent!!.get()!!.indexOfChild(contentView)
            parent!!.get()!!.removeViewAt(index)
            parent!!.get()!!.addView(newContentView, index)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setMessage(message: Any) {
        if(snackbar == null){
            return
        }
        snackbar!!.setText(message.toString()).show()
    }

}
