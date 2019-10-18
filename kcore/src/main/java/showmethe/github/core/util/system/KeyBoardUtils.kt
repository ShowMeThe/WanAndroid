package showmethe.github.core.util.system

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * 软键盘工具类
 */

/**
 * 关闭软键盘
 *
 * @param editText 输入框
 *
 */
fun Context.closeKeyboard(editText: EditText): Boolean {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Context.hideSoftKeyboard() {
    val activity = this as Activity
    val view = activity.currentFocus
    if (view != null) {
        val inputMethodManager = getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Context.openKeyboard( editText: EditText) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

