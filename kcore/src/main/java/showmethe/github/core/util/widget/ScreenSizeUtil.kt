package showmethe.github.core.util.widget

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.widget
 */

class ScreenSizeUtil{

   companion object {

       var width = 0
       var height  = 0

       fun getWidth(context: Context) : Int{
           if(width == 0){
               val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
               val dm = DisplayMetrics()
               manager.defaultDisplay.getMetrics(dm)
               width = dm.widthPixels
           }
           return width
       }


       fun getHeight(context: Context) : Int{
           if(height == 0){
               val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
               val dm = DisplayMetrics()
               manager.defaultDisplay.getMetrics(dm)
               height = dm.heightPixels
           }
           return height
       }
   }
}