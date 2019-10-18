package showmethe.github.core.widget.citypicker.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:53
 * Package Name:showmethe.github.core.widget.citypicker.bean
 */

data class DistrictBean(val id: String = "", var name: String = "") : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DistrictBean> = object : Parcelable.Creator<DistrictBean> {
            override fun createFromParcel(source: Parcel): DistrictBean = DistrictBean(source)
            override fun newArray(size: Int): Array<DistrictBean?> = arrayOfNulls(size)
        }
    }
}