package com.showmethe.galley.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.showmethe.galley.database.dto.UserDto

/**
 *  com.showmethe.galley.database.dao
 *  2019/10/18
 *  showMeThe
 *  23:39
 */
@Dao
interface UserDataDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun register(dto : UserDto)

    @Query("select * from UserDto where  userName = :userName ")
    fun getUserByName(userName : String) : LiveData<UserDto>


}