package com.example.p2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.p2.db.AppDatabase;

import java.util.Date;
@Entity(tableName = AppDatabase.INVENTORYLOG_TABLE)
public class InventoryLog {
    @PrimaryKey(autoGenerate = true)    //database automatically supply key
                                        // value when inserting into database
    private int mLogID;
    private String mTitle;
    private double mPrice;
    private int mQuantity;
    private Date mDate;

    private int mUserId;

    public InventoryLog(String mTitle, double mPrice, int mQuantity, int userId) {
        this.mTitle = mTitle;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;

        mDate = new Date();

        mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getLogID() {
        return mLogID;
    }

    public void setLogID(int mLogID) {
        this.mLogID = mLogID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public String toString() {
        String output;

        output = mTitle + " " + mPrice + " : " + mQuantity;
        output += "\n";
        output += getDate();
        output += "\n";
        output += "userId == " + mUserId;

        //include mLogId???

        return output;
    }


}
