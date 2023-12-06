package com.example.p2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.p2.InventoryLog;
import com.example.p2.User;
import com.example.p2.db.typeConverters.DateTypeConverter;

@Database(entities = {InventoryLog.class, User.class}, version = 2)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "INVENTORYLOG_DATABASE";
    public static final String INVENTORYLOG_TABLE = "INVENTORYLOG_TABLE";
    public static final String USER_TABLE = "USER_TABLE";
    public abstract InventoryLogDAO getInventoryLogDAO();
}
