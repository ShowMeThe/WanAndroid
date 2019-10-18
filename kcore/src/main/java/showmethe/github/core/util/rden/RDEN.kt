package showmethe.github.core.util.rden

import android.content.Context
import androidx.room.Room
import org.jetbrains.annotations.NotNull

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.rden
 */

class RDEN private constructor(){

   companion object{


       private lateinit var creator : DatabaseCreator


       fun build(context : Context){
           creator = Room.databaseBuilder(context.applicationContext,DatabaseCreator::class.java,"roombean")
                   .allowMainThreadQueries().build()
       }


       fun getRoomDao() : RoomDao {
           return creator.roomDao()
       }


       fun put(@NotNull key: String,@NotNull value : String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.stringValue = value
           getRoomDao().put(bean)
       }


       fun put(@NotNull key: String,@NotNull value : Boolean){
           val bean = RoomBean()
           bean.storeKey = key
           bean.booleanValue = value
           getRoomDao().put(bean)
       }


       fun put(@NotNull key: String,@NotNull value : Int){
           val bean = RoomBean()
           bean.storeKey = key
           bean.integerValue = value
           getRoomDao().put(bean)
       }


       fun put(@NotNull key: String,@NotNull value : Long){
           val bean = RoomBean()
           bean.storeKey = key
           bean.longValue = value
           getRoomDao().put(bean)
       }


       fun put(@NotNull key: String,@NotNull value : ByteArray){
           val bean = RoomBean()
           bean.storeKey = key
           bean.bytesValue = value
           getRoomDao().put(bean)
       }

       fun put(@NotNull key: String,@NotNull value : ArrayList<*>){
           val bean = RoomBean()
           bean.storeKey = key
           bean.listValue = value
           getRoomDao().put(bean)
       }

       fun deleteString(@NotNull key: String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.stringValue = null
           getRoomDao().put(bean)
       }

       fun deleteBoolean(@NotNull key: String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.booleanValue = null
           getRoomDao().put(bean)
       }

       fun deleteInt(@NotNull key: String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.integerValue = null
           getRoomDao().put(bean)
       }

       fun deleteLone(@NotNull key: String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.longValue = null
           getRoomDao().put(bean)
       }

       fun deleteByteArray(@NotNull key: String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.bytesValue = null
           getRoomDao().put(bean)
       }

       fun deleteArrayList(@NotNull key: String){
           val bean = RoomBean()
           bean.storeKey = key
           bean.listValue = null
           getRoomDao().put(bean)
       }


       fun deleteAll(@NotNull key: String){
           getRoomDao().delete(key)
       }


       inline fun <reified T>get(key : String,default: T) : T {
           try {
               val clazz = T::class.java
               when {
                   clazz.name == String :: class.java.name -> {
                       val bean =  getRoomDao().get(key)
                       return if(bean?.stringValue.isNullOrEmpty()){
                           default
                       }else{
                           bean?.stringValue as T
                       }
                   }
                   clazz.name == "java.lang.Boolean" -> {
                       val bean =  getRoomDao().get(key)
                       return if(bean?.booleanValue == null){
                           default
                       }else{
                           bean.booleanValue as T
                       }
                   }
                   clazz.name == "java.lang.Integer" -> {
                       val bean =  getRoomDao().get(key)
                       return if(bean?.integerValue == null){
                           default
                       }else{
                           bean.integerValue as T
                       }
                   }
                   clazz.name == "java.lang.Long" -> {
                       val bean =  getRoomDao().get(key)
                       return if(bean?.longValue == null){
                           default
                       }else{
                           bean.longValue as T
                       }
                   }
                   clazz.name == ByteArray :: class.java.name -> {
                       val bean =  getRoomDao().get(key)
                       return if(bean?.bytesValue == null){
                           default
                       }else{
                           bean.bytesValue as T
                       }
                   }
                   clazz.name == "java.util.ArrayList" -> {
                       val bean =  getRoomDao().get(key)
                       return if(bean?.listValue == null){
                           default
                       }else{
                           bean.listValue as T
                       }
                   }
               }
           }catch (e : Exception){
               return default
           }
           return default
       }
   }
}
