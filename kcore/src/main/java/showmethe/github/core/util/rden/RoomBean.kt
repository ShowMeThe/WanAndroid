package showmethe.github.core.util.rden

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.rden
 */

@Entity(tableName = "RoomBean")
@TypeConverters(Converters::class)
class RoomBean {


    @PrimaryKey
    lateinit var storeKey: String
    var stringValue: String? = null
    var booleanValue: Boolean? = null
    var longValue: Long? = null
    var integerValue: Int? = null
    var bytesValue: ByteArray? = null
    var listValue: ArrayList<*>? = null
}
