package showmethe.github.core.widget.citypicker.picker

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

import androidx.databinding.ObservableArrayList
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import showmethe.github.core.R
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.widget.citypicker.bean.CityBean
import showmethe.github.core.widget.citypicker.bean.DistrictBean
import showmethe.github.core.widget.citypicker.bean.ProvinceBean
import showmethe.github.core.widget.citypicker.helper.CityParseHelper

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:53
 * Package Name:showmethe.github.core.widget.citypicker.picker
 */

class CityPicker : DialogFragment() {

    private lateinit var mContext : Context
    private var rg: RadioGroup? = null
    private var rb1: RadioButton? = null
    private var rb2: RadioButton? = null
    private var rb3: RadioButton? = null
    private var radioButtons: Array<RadioButton>? = null
    private var rv: RecyclerView? = null
    private var parseHelper: CityParseHelper? = null
    private var mProvinceAdapter: ProvinceAdapter? = null
    private var mCityAdapter: CityAdapter? = null
    private var mAreaAdapter: AreaAdapter? = null
    private var tvConfirm: TextView? = null
    private var tvCancel: TextView? = null
    private var provinceBean: ProvinceBean? = null
    private var cityBean: CityBean? = null
    private var districtBean: DistrictBean? = null
    private val provinceList = ObservableArrayList<ProvinceBean>()

    private val provinceListener = object : BaseRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            tvConfirm!!.visibility = View.VISIBLE
            provinceBean = mProvinceAdapter!!.mData[position]
            mProvinceAdapter!!.updateSelectedPosition(position)
            updateTab(0, provinceBean!!.name)
            if (mCityAdapter == null) {
                mCityAdapter = CityAdapter(mContext, provinceBean!!.cityList)
                mCityAdapter!!.setOnItemClickListener(cityListener)
            } else {
                mCityAdapter!!.mData = provinceBean!!.cityList
            }
            rv!!.adapter = mCityAdapter
        }
    }

    private val cityListener = object : BaseRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            cityBean = mCityAdapter!!.mData[position]
            mCityAdapter!!.updateSelectedPosition(position)

            updateTab(1, cityBean!!.name)
            if (mAreaAdapter == null) {
                mAreaAdapter = AreaAdapter(mContext, cityBean!!.cityList)
                mAreaAdapter!!.setOnItemClickListener(areaListener)
            } else {
                mAreaAdapter!!.mData = cityBean!!.cityList
            }
            rv!!.adapter = mAreaAdapter

        }
    }

    private val areaListener = object : BaseRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            districtBean = mAreaAdapter!!.mData[position]
            updateTab(2, districtBean!!.name)
            mAreaAdapter!!.updateSelectedPosition(position)

        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(context!!)
        val view = View.inflate(context, R.layout.dialog_city_picker, null)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(true)

        val window = dialog.window
        if (window != null) {
            val dm = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(dm)
            window.setLayout(dm.widthPixels, window.attributes.height)
            window.setBackgroundDrawable(ColorDrawable(0x00000000))
            window.setGravity(Gravity.BOTTOM)
            window.setWindowAnimations(R.style.AnimBottom)
        }

        init(view)
        initData()
        initAdapter()


        return dialog
    }


    private fun init(view: View) {
        rg = view.findViewById(R.id.rg)
        rb1 = view.findViewById(R.id.rb1)
        rb2 = view.findViewById(R.id.rb2)
        rb3 = view.findViewById(R.id.rb3)
        radioButtons = arrayOf(rb1!!, rb2!!, rb3!!)
        tvConfirm = view.findViewById(R.id.tvConfirm)
        tvCancel = view.findViewById(R.id.tvCancel)
        rv = view.findViewById(R.id.rv)
        rv!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        tvCancel!!.setOnClickListener { dismiss() }
        tvConfirm!!.visibility = View.INVISIBLE
        updateIsClickable()
    }


    private fun initData() {
        if (parseHelper == null) {
            parseHelper = CityParseHelper()
        }
        if (parseHelper!!.provinceBeanArrayList.isEmpty()) {
            parseHelper!!.initData(mContext)
        }

        provinceList.addAll(parseHelper!!.provinceBeanArrayList)

    }


    private fun initAdapter() {
        mProvinceAdapter = ProvinceAdapter(context!!, provinceList)
        rv!!.adapter = mProvinceAdapter

        mProvinceAdapter!!.setOnItemClickListener(provinceListener)


        tvConfirm!!.setOnClickListener {
            if (onCityPickListener != null) {
                onCityPickListener?.invoke(provinceBean, cityBean, districtBean)
            }
            dismiss()
        }

        rb1!!.setOnClickListener {
            if (mProvinceAdapter != null) {
                rv!!.adapter = mProvinceAdapter
                rv!!.smoothScrollToPosition(if (mProvinceAdapter!!.selectedPosition == -1) 0 else mProvinceAdapter!!.selectedPosition)
            }

            if (mAreaAdapter != null) {
                mAreaAdapter!!.updateSelectedPosition(-1)
            }
            if (mCityAdapter != null) {
                mCityAdapter!!.updateSelectedPosition(-1)
            }
            clearTab(0)
        }




        rb2!!.setOnClickListener {
            if (mCityAdapter != null) {
                rv!!.adapter = mCityAdapter
                rv!!.smoothScrollToPosition(if (mCityAdapter!!.selectedPosition == -1) 0 else mCityAdapter!!.selectedPosition)
            }
            if (mAreaAdapter != null) {
                mAreaAdapter!!.updateSelectedPosition(-1)
            }

            clearTab(1)
        }

        rb3!!.setOnClickListener {
            if (mAreaAdapter != null) {
                rv!!.adapter = mAreaAdapter
                rv!!.smoothScrollToPosition(if (mAreaAdapter!!.selectedPosition == -1) 0 else mAreaAdapter!!.selectedPosition)
            }
        }


    }


    private var onCityPickListener: ((provinceBean: ProvinceBean?, cityBean: CityBean?, districtBean: DistrictBean?)->Unit)? = null
    fun setOnCityPickListener(onCityPickListener: ((provinceBean: ProvinceBean?, cityBean: CityBean?, districtBean: DistrictBean?)->Unit)) {
        this.onCityPickListener = onCityPickListener
    }


    private fun updateTab(position: Int, s: String) {
        radioButtons!![position].text = s
        updateIsClickable()
    }


    private fun clearTab(size: Int) {
        for (i in radioButtons!!.indices) {
            if (i > size) {
                radioButtons!![i].isChecked = false
                radioButtons!![i].isClickable = false
                radioButtons!![i].text = ""
            }
        }
    }


    private fun updateIsClickable() {
        for (i in radioButtons!!.indices) {
            if (radioButtons!![i].text.toString().isEmpty()) {
                radioButtons!![i].isClickable = false
                radioButtons!![i].isChecked = false
            } else {
                radioButtons!![i].isClickable = true
                radioButtons!![i].isChecked = true
            }
        }

    }


}
