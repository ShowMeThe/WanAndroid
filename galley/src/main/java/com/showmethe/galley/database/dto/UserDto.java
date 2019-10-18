package com.showmethe.galley.database.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * com.showmethe.galley.database.dto
 * 2019/10/18
 * showMeThe
 * 23:38
 */
@Entity(tableName = "UserDto")
public class UserDto {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String userName =  "";
    private String  password =  "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
