package com.showmethe.galley.database.dto;


import androidx.databinding.ObservableArrayList;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.database.source.converters.ArraysConverters;

import java.util.ArrayList;

@Entity(tableName = "PhotoWallDto")
@TypeConverters(ArraysConverters.class)
public class PhotoWallDto {

    @PrimaryKey
    private int id = 0;
    private int selectPosition = 0;
    private ObservableArrayList<String> imageList;
    private String  avatar = "";
    private String userName = "";
    protected boolean like = false;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObservableArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ObservableArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
