package showmethe.github.core.util.rden

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.rden
 */

@Database(entities = [RoomBean::class],version = 1)
abstract class DatabaseCreator : RoomDatabase() {

   abstract fun roomDao() : RoomDao

}