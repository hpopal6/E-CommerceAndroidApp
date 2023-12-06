/**
 * @title CST 338 Project 02: Part 02 Login and Landing Page
 * @author Harris Popal
 * @date 12/6/2023
 * @abstract This is the User class (POJO)
 */
package com.example.p2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.p2.db.AppDatabase;

import java.util.List;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;
    private String mUserName;
    private String mPassword;
    private boolean mAdmin;
    //private List<String> adminList;


    public User(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;

        this.mAdmin = checkIfAdmin();
    }

    private boolean checkIfAdmin(){
        if(getUserName().equals("admin2")){
            return true;
        }
        return false;
    }
    public boolean isAdmin() {
        return mAdmin;
    }

    public void setAdmin(boolean mAdmin) {
        this.mAdmin = mAdmin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
