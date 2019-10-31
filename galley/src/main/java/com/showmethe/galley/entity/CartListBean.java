package com.showmethe.galley.entity;

import androidx.databinding.ObservableArrayList;

/**
 * com.showmethe.galley.entity
 * 2019/10/31
 * 22:48
 */
public class CartListBean {

    private int orderId = 0;
    private String goodsName = "";
    private String coverImg = "";
    private String goodDes = "";
    private String price;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getGoodDes() {
        return goodDes;
    }

    public void setGoodDes(String goodDes) {
        this.goodDes = goodDes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
