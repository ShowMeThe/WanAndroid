package com.showmethe.galley.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.showmethe.galley.database.dto.PhotoWallDto

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 14:28
 * Package Name:com.showmethe.galley.database.dao
 */
interface PhotoWallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPhotoBean(bean : PhotoWallDto)

    @Query("select * from PhotoWallDto order by id DESC")
    fun getPhotoBean() : LiveData<List<PhotoWallDto>>

    @Query("update  PhotoWallDto set `like` = :like where id = :id")
    fun setIdLike(id:Int,like: Boolean)

}