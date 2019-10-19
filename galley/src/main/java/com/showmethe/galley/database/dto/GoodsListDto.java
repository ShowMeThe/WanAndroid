package com.showmethe.galley.database.dto;


import androidx.databinding.ObservableArrayList;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.database.source.converters.ArraysConverters;

@Entity(tableName = "GoodsListDto")
@TypeConverters({ArraysConverters.class})
public class GoodsListDto {

    @PrimaryKey(autoGenerate =  true)
    private int id = 0;
    private String goodsName = "";
    private String coverImg = "";
    private ObservableArrayList<String> goodsImg;
    private String goodDes = "";
    private String price;

    @Ignore
    private int vibrantColor = -1;

    public int getVibrantColor() {
        return vibrantColor;
    }

    public void setVibrantColor(int vibrantColor) {
        this.vibrantColor = vibrantColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ObservableArrayList<String> getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(ObservableArrayList<String> goodsImg) {
        this.goodsImg = goodsImg;
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
