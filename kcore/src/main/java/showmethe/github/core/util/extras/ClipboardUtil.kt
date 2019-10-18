package showmethe.github.core.util.extras

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.extras
 */
fun copyTextToClipboard(context:Context,text : CharSequence ){
    val clipboard = context.getSystemService(RxAppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager;
    val clipData = ClipData.newPlainText("text","$text")
    clipboard.setPrimaryClip(clipData)

}

fun getTextFromClipboard(context:Context) : String{
    val clipboard = context.getSystemService(RxAppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData  = clipboard.primaryClip
    return  if( clipData!=null && clipData.itemCount>0 ){
        clipData.getItemAt(0)?.text.toString()
    }else{
        ""
    }
}

fun copyUriToClipboard(context:Context,uri : Uri){
    val clipboard = context.getSystemService(RxAppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager;
    val clipData = ClipData.newUri(context.contentResolver,"uri",uri)
    clipboard.setPrimaryClip(clipData)
}

fun getUriFromClipboard(context:Context) : Uri{
    val clipboard = context.getSystemService(RxAppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager;
    val clipData  = clipboard.primaryClip
    return if( clipData!=null && clipData.itemCount>0){
        clipData.getItemAt(0)?.uri!!
    }else{
        Uri.EMPTY
    }

}