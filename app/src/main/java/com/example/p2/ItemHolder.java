package com.example.p2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.p2.db.AppDatabase;

@Entity(tableName = AppDatabase.ITEMHOLDER_TABLE)
public class ItemHolder {
    @PrimaryKey(autoGenerate = true)
    private int mItemHolderId;

    private int mItemId;
    private String mTitle;
    private int mUserId;

    private String mUsername;
    private int mQuantity;

    public ItemHolder(int mItemId, String mTitle, int mUserId, String mUsername, int mQuantity) {
        this.mItemId = mItemId;
        this.mUserId = mUserId;
        this.mTitle = mTitle;
        this.mUsername = mUsername;
        this.mQuantity = mQuantity;
    }

    public int getItemHolderId() {
        return mItemHolderId;
    }

    public void setItemHolderId(int mItemHolderId) {
        this.mItemHolderId = mItemHolderId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int mItemId) {
        this.mItemId = mItemId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    @Override
    public String toString() {
        String output;

        output = "ItemHolder: " + mUsername + "\n"
                + "Quantity: " + mQuantity + "\n";
                //+ "itemId: " + mItemId;

        return output;
    }
}
