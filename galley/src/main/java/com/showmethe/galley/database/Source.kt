package com.showmethe.galley.database

/**
 * Author: showMeThe
 * Update Time: 2019/10/18 14:18
 * Package Name:com.showmethe.galley.database
 */
class Source{

    companion object{
        private var INSTANT : Source? = null

        fun get() : Source{
            if(INSTANT == null){
                INSTANT = Source()
            }
            return   INSTANT!!
        }
    }

    private val bannerSource =  ArrayList<String>()
    private val userName =  ArrayList<String>()
    private val content = ArrayList<String>()

    fun init(){
        bannerSource.apply {
            add("http://image1.xyzs.com/upload/a6/1c/1450580015244844/20151224/145089874795426_0.jpg")
            add("http://image2.xyzs.com/upload/9f/f3/1449368181406009/20151209/144960051320867_0.jpg")
            add("http://image3.xyzs.com/upload/b9/40/1449104703418440/20151205/144925600471264_0.jpg")
            add("http://image1.xyzs.com/upload/9b/b2/1450314878985387/20151219/145046718515607_0.jpg")
            add("http://image2.xyzs.com/upload/fc/6a/1450315960904658/20151219/145046718037409_0.jpg")
            add("http://image4.xyzs.com/upload/03/f7/1449978315650125/20151217/145028981364776_0.jpg")
            add("http://image1.xyzs.com/upload/b2/88/1450055515760915/20151217/145028981024042_0.jpg")
            add("http://image2.xyzs.com/upload/f9/f0/1450057329342039/20151217/145028980312438_0.jpg")
            add("http://image1.xyzs.com/upload/6d/7a/1450150493535765/20151217/145028979798709_0.jpg")
            add("http://image4.xyzs.com/upload/03/f7/1449978315650125/20151217/145028981364776_0.jpg")
            add("http://image1.xyzs.com/upload/b2/88/1450055515760915/20151217/145028981024042_0.jpg")
            add("http://image2.xyzs.com/upload/17/e9/1450141786752453/20151217/145028979377673_0.jpg")
            add("http://image4.xyzs.com/upload/58/95/1450141785169453/20151217/145028979233343_0.jpg")
            add("http://image2.xyzs.com/upload/38/b9/1449796978515895/20151213/144994905652056_0.jpg")
            add("http://image1.xyzs.com/upload/bc/0e/1449797202925662/20151213/144994905553024_0.jpg")
            add("http://image1.xyzs.com/upload/18/d1/1449797201910142/20151213/144994905075729_0.jpg")
            add("http://image1.xyzs.com/upload/75/3a/1450918639776042/20151229/145139973539453_0.jpg")
            add("http://image2.xyzs.com/upload/30/fc/1450400984945645/20151224/145090279520818_0.jpg")
            add("http://image1.xyzs.com/upload/09/b4/1450405158764323/20151224/145090278841619_0.jpg")
            add("http://image1.xyzs.com/upload/ce/3c/1450405157510140/20151224/145090278730419_0.jpg")
            add("http://image3.xyzs.com/upload/48/b1/1450405156721242/20151224/145090278474235_0.jpg")
            add("http://image3.xyzs.com/upload/3d/07/1450492652980419/20151224/145090278371813_0.jpg")
            add("http://image4.xyzs.com/upload/58/b4/1450492650570007/20151224/145090278159558_0.jpg")
            add("http://image1.xyzs.com/upload/1a/b1/1450492649469952/20151224/145090277979009_0.jpg")
            add("http://image1.xyzs.com/upload/12/d8/1450580008357922/20151224/145090277258685_0.jpg")
            add("http://image2.xyzs.com/upload/9e/0e/1450580006395541/20151224/145090277014104_0.jpg")
            add("http://image4.xyzs.com/upload/79/0d/1450661268823426/20151224/145090275549310_0.jpg")
            add("http://image3.xyzs.com/upload/fd/d4/1450660354737808/20151224/145090275350248_0.jpg")
        }


        userName.apply {
            add("Abbott")
            add("Alfred")
            add("Edwiin")
            add("Barlow")
            add("Basil")
            add("Carey")
            add("Beck")
            add("Evan")
            add("@")
            add("$")
            add("^")
            add("*@@")
            add("&_")
            add("%hh*_")
        }


        content.apply {
            add("Louis got a new book. The book was about animals. Louis loved animals. The book had lots of pictures. It had pictures of dogs and cats.")
            add("Elizabeth washes her hands every day. She likes to wash her hands. She washes her hands with soap and water. " +
                    "She uses soap and water to wash her hands. She uses warm water and soap.")
            add("Elizabeth has very clean hands. She does not have many germs on her hands. Germs cannot live on her clean hands.")
            add("The purpose of a Christmas tree is to have a central location to place gifts.")
            add("Father's Day is a holiday observed in the United States to honor fathers, fatherhood, and other paternal figures like grandfathers," +
                    " stepfathers, and uncles. It is observed on the third Sunday in June.")
        }


    }


    fun getBanner() = bannerSource

    fun getUserName() = userName

    fun getContent() = content

}