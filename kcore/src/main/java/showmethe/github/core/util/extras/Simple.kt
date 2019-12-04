package showmethe.github.core.util.extras

import android.animation.Animator
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.adapters.TextViewBindingAdapter
import com.google.android.material.tabs.TabLayout

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.extras
 */

fun EditText.textWatcher(textWatch: SimpleTextWatcher.()->Unit){
    val simpleTextWatcher = SimpleTextWatcher()
    textWatch.invoke(simpleTextWatcher)
    addTextChangedListener(object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            simpleTextWatcher.afterText?.invoke(s,s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            simpleTextWatcher.beforeText?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            simpleTextWatcher.onTextChanged?.invoke(s, start, before, count)
        }
    })
}

 class  SimpleTextWatcher() {
     var afterText: (Editable?.(s: Editable?)->Unit)? = null
     fun afterTextChanged(afterText: (Editable?.(s: Editable?)->Unit)){
         this.afterText = afterText
     }
     var beforeText:((s: CharSequence?, start: Int, count: Int, after: Int)->Unit)? = null
     fun beforeTextChanged(beforeText:((s: CharSequence?, start: Int, count: Int, after: Int)->Unit)){
         this.beforeText = beforeText
     }
     var  onTextChanged : ((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)? = null
     fun onTextChanged(onTextChanged : ((s: CharSequence?, start: Int, before: Int, count: Int)->Unit)){
         this.onTextChanged = onTextChanged
     }

}

open class SimpleAnimatorListener : Animator.AnimatorListener{
    override fun onAnimationRepeat(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationStart(p0: Animator?) {
    }

}


open class SimpleAnimationListener : Animation.AnimationListener{
    override fun onAnimationRepeat(p0: Animation?) {
    }

    override fun onAnimationEnd(p0: Animation?) {
    }

    override fun onAnimationStart(p0: Animation?) {
    }

}

open class SimpleTabSelectedListener  : TabLayout.OnTabSelectedListener{
    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
    }

}


open class SimpleLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }
}