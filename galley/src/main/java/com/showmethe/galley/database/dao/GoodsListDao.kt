package com.showmethe.galley.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.showmethe.galley.database.dto.CartDto
import com.showmethe.galley.database.dto.GoodsListDto
import com.showmethe.galley.entity.CartListBean

/**
 * Author: showMeThe
 * Update Time: 2019/10/19
 * Package Name:com.showmethe.galley.database.dao
 */
@Dao
interface GoodsListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoods(dto : GoodsListDto)


    @Query("select * from GoodsListDto limit :page,:pageSize  ")
    fun findByPage(page:Int,pageSize:Int) : LiveData<List<GoodsListDto>>


    @Query("select * from GoodsListDto")
    fun findAllGoods() : List<GoodsListDto>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCart(dto: CartDto)

    @Query("select orderId,goodsName,coverImg,goodDes,price from GoodsListDto inner join CartDto where GoodsListDto.id == CartDto.goodsId")
    fun getCartList() : LiveData<List<CartListBean>>
}