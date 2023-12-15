package com.example.p2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.p2.db.AppDatabase;

import java.util.Date;

@Entity(tableName = AppDatabase.ORDER_TABLE)
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private int mOrderID;
    private String mTitle;
    private double mPrice;
    private int mQuantity;
    private Date date;

    public Order(int mUserId, String mTitle, double mPrice, int mQuantity) {
        this.mUserId = mUserId;
        this.mTitle = mTitle;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getmOrderID() {
        return mOrderID;
    }

    public void setmOrderID(int mOrderID) {
        this.mOrderID = mOrderID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
