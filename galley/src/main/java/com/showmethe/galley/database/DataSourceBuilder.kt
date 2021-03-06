package com.showmethe.galley.database

import android.content.Context
import androidx.room.Room
import com.showmethe.galley.database.dao.GoodsListDao
import com.showmethe.galley.database.dao.PhotoWallDao
import com.showmethe.galley.database.dao.UserDataDao

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 14:30
 * Package Name:com.showmethe.galley.database
 */
class DataSourceBuilder {

    companion object{

        private lateinit var creator : AppDataBaseCreator

        fun build(context : Context){
            creator = Room.databaseBuilder(context.applicationContext,AppDataBaseCreator::class.java,"app_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        }

        fun getPhotoWall() : PhotoWallDao {
            return  creator.getPhotoDao()
        }

        fun getUser() : UserDataDao {
            return  creator.getUserDao()
        }

        fun getGoods() : GoodsListDao {
            return  creator.getGoodsDao()
        }

    }
}