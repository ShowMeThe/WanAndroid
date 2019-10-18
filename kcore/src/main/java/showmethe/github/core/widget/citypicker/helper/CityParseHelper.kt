package showmethe.github.core.widget.citypicker.helper

import android.content.Context
import android.util.ArrayMap

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


import java.util.ArrayList

import androidx.databinding.ObservableArrayList
import showmethe.github.core.util.assetFile.AssetFile
import showmethe.github.core.widget.citypicker.AssestConstant
import showmethe.github.core.widget.citypicker.bean.CityBean
import showmethe.github.core.widget.citypicker.bean.DistrictBean
import showmethe.github.core.widget.citypicker.bean.ProvinceBean

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:53
 * Package Name:showmethe.github.core.widget.citypicker.helper
 */

class CityParseHelper {

    /**
     * 省份数据
     */
    var provinceBeanArrayList: ObservableArrayList<ProvinceBean> = ObservableArrayList()

    /**
     * 城市数据
     */
    private var cityBeanArrayList: ObservableArrayList<ArrayList<CityBean>>? = null

    /**
     * 地区数据
     */
    var districtBeanArrayList: ObservableArrayList<ObservableArrayList<ObservableArrayList<DistrictBean>>>? = null

    var provinceBeenArray: Array<ProvinceBean?>?= null

    var provinceBean: ProvinceBean? = null

    var cityBean: CityBean? = null

    var districtBean: DistrictBean? = null

    /**
     * key - 省 value - 市
     */
    private val mPro_CityMap = ArrayMap<String,Array<CityBean?>>()

    /**
     * key - 市 values - 区
     */
    private val mCity_DisMap = ArrayMap<String,Array<DistrictBean?>>()
    /**
     * key - 区 values
     */
    private val mDisMap = ArrayMap<String,DistrictBean>()


    /**
     * 初始化数据，解析json数据
     */
    fun initData(context: Context) {
        val cityJson = AssetFile.getJson(context, AssestConstant.CITY_DATA)
        val type = object : TypeToken<ObservableArrayList<ProvinceBean>>() {}.type

        provinceBeanArrayList = Gson().fromJson(cityJson, type)

        cityBeanArrayList = ObservableArrayList()
        districtBeanArrayList = ObservableArrayList()


        //*/ 初始化默认选中的省、市、区，默认选中第一个省份的第一个市区中的第一个区县
        if (!provinceBeanArrayList.isEmpty()) {
            provinceBean = provinceBeanArrayList[0]
            val cityList = provinceBean!!.cityList
            if (!cityList.isEmpty() && cityList.size > 0) {
                cityBean = cityList[0]
                val districtList = cityBean!!.cityList
                if (!districtList.isEmpty() && districtList.size > 0) {
                    districtBean = districtList[0]
                }
            }
        }

        //省份数据
        provinceBeenArray = arrayOfNulls(provinceBeanArrayList.size)

        for (p in provinceBeanArrayList.indices) {

            //遍历每个省份
            val itemProvince = provinceBeanArrayList[p]

            //每个省份对应下面的市
            val cityList = itemProvince.cityList

            //当前省份下面的所有城市
            val cityNames = arrayOfNulls<CityBean>(cityList.size)

            //遍历当前省份下面城市的所有数据
            for (j in cityList.indices) {

                cityNames[j] = cityList[j]

                //当前省份下面每个城市下面再次对应的区或者县
                val districtList = cityList[j].cityList
                val distrinctArray = arrayOfNulls<DistrictBean>(districtList.size)

                for (k in districtList.indices) {

                    // 遍历市下面所有区/县的数据
                    val districtModel = districtList[k]

                    //存放 省市区-区 数据
                    mDisMap[itemProvince.name + cityNames[j]!!.name + districtList[k].name] = districtModel

                    distrinctArray[k] = districtModel

                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mCity_DisMap[itemProvince.name + cityNames[j]!!.name] = distrinctArray

            }

            // 省-市的数据，保存到mCitisDatasMap
            mPro_CityMap[itemProvince.name] = cityNames

            cityBeanArrayList!!.add(cityList)

            //只有显示三级联动，才会执行
            val array2DistrictLists = ObservableArrayList<ObservableArrayList<DistrictBean>>()

            for (c in cityList.indices) {
                val (_, _, cityList1) = cityList[c]
                array2DistrictLists.add(cityList1)
            }
            districtBeanArrayList!!.add(array2DistrictLists)


            //赋值所有省份的名称
            provinceBeenArray!![p] = itemProvince

        }

    }

}
