package com.showmethe.galley.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.showmethe.galley.database.dao.GoodsListDao
import com.showmethe.galley.database.dao.PhotoWallDao
import com.showmethe.galley.database.dao.UserDataDao
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.database.dto.PhotoWallDto
import com.showmethe.galley.database.dto.UserDto

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 14:30
 * Package Name:com.showmethe.galley.database
 */
@Database(entities = [PhotoWallDto::class, UserDto::class, GoodsListDto::class],version = 1)
abstract class AppDataBaseCreator : RoomDatabase() {

    abstract fun getPhotoDao() : PhotoWallDao

    abstract fun getUserDao() : UserDataDao

    abstract fun getGoodsDao() : GoodsListDao

}