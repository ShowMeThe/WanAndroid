package com.showmethe.galley.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * com.showmethe.galley.database.dto
 * 2019/10/31
 * 22:30
 */
@Entity(foreignKeys = [ForeignKey(entity = GoodsListDto::class,parentColumns = ["id"],childColumns = ["goodsId"],onDelete = ForeignKey.CASCADE)]
        ,indices = [androidx.room.Index(value = ["goodsId"], unique = true)])
class CartDto {

    @PrimaryKey(autoGenerate = true)
    var orderId = 0
    var goodsId = 0


}
