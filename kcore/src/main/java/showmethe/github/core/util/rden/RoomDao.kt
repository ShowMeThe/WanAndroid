package showmethe.github.core.util.rden

import androidx.room.*

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.rden
 */

@Dao
interface RoomDao{


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(bean : RoomBean)

    @Query("select * from RoomBean where storeKey  = (:key)")
    fun get(key :String ) : RoomBean?


    @Query("delete from RoomBean  where storeKey = (:key)")
    fun delete(key :String )
}
