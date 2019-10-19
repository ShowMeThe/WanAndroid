package com.showmethe.galley.database.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * com.showmethe.galley.database.dto
 * 2019/10/18
 * showMeThe
 * 23:38
 */
@Entity(tableName = "UserDto")
public class UserDto {


    @PrimaryKey
    @NotNull
    private String userName =  "";
    private String  password =  "";


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
