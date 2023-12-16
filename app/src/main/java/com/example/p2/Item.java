package com.example.p2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.p2.db.AppDatabase;

@Entity(tableName = AppDatabase.ITEM_TABLE)
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int mItemId;

    private String mTitle;
    //private double mPrice;
    private int mQuantity;

    private int mUserId;

    public Item(String mTitle, int mQuantity, int userId) {
        this.mTitle = mTitle;
        this.mQuantity = mQuantity;

        mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /*public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }*/

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int mItemId) {
        this.mItemId = mItemId;
    }

    @Override
    public String toString() {
        String output;

        output = "Item: " + mTitle + "\n"
                + "Quantity: " + mQuantity + "\n"
                + "itemId: " + mItemId;

        return output;
    }
}
