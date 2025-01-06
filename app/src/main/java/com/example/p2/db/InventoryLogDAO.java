/**
 * @title CST 338 Project 02: Part 02 Login and Landing Page
 * @author Harris Popal
 * @date 12/6/2023
 * @abstract This is the DAO (Data Access Object) for Inventory Log
 */
package com.example.p2.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.p2.InventoryLog;
import com.example.p2.Item;
import com.example.p2.ItemHolder;
import com.example.p2.User;

import org.checkerframework.checker.formatter.qual.InvalidFormat;

import java.util.List;

@Dao
public interface InventoryLogDAO {

    @Insert
    void insert(InventoryLog... inventoryLogs);

    @Update
    void update(InventoryLog... inventoryLogs);

    @Delete
    void delete(InventoryLog inventoryLog);

    @Query("SELECT * FROM " + AppDatabase.INVENTORYLOG_TABLE + " ORDER BY mDate")
    List<InventoryLog> getAllInventoryLogs();
    @Query("SELECT * FROM " + AppDatabase.INVENTORYLOG_TABLE + " WHERE mLogID = :logId")
    List<InventoryLog> getInventoryLogsById(int logId);

    @Query("SELECT * FROM " + AppDatabase.INVENTORYLOG_TABLE + " WHERE mUserID = :userId ORDER BY mDate DESC")
    List<InventoryLog> getInventoryLogsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.INVENTORYLOG_TABLE + " WHERE mTitle = :title")
    List<InventoryLog> getInventoryLogsByTitle(String title);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(Item...items);

    @Update
    void update(Item...items);

    @Delete
    void delete(Item...items);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE)
    List<Item> getAllItems();

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mTitle = :title")
    Item getItemByTitle(String title);
    //List<Item> getItemByTitle(String title);


    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mUserId = :userId")
    List<Item> getItemsByUserId(int userId);

    @Insert
    void insert(ItemHolder...itemHolders);

    @Update
    void update(ItemHolder...itemHolders);

    @Delete
    void delete(ItemHolder...itemHolders);

    @Query("SELECT * FROM " + AppDatabase.ITEMHOLDER_TABLE)
    List<ItemHolder> getAllItemHolders();

    @Query("SELECT * FROM " + AppDatabase.ITEMHOLDER_TABLE + " WHERE mTitle = :title")
    List<ItemHolder> getItemHolderByTitle(String title);

    @Query("SELECT * FROM " + AppDatabase.ITEMHOLDER_TABLE + " WHERE mItemId = :itemId")
    List<ItemHolder> getItemHoldersByItemId(int itemId);

    @Query("SELECT * FROM " + AppDatabase.ITEMHOLDER_TABLE + " WHERE mItemHolderId = :itemHolderId")
    ItemHolder getItemHolderByItemHolderId(int itemHolderId);

}
