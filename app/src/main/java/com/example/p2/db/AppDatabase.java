package com.example.p2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import java.util.HashMap;


import com.example.p2.InventoryLog;
import com.example.p2.Item;
import com.example.p2.User;
import com.example.p2.db.typeConverters.DateTypeConverter;

@Database(entities = {InventoryLog.class, User.class, Item.class}, version = 3)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "INVENTORYLOG_DATABASE";
    public static final String INVENTORYLOG_TABLE = "INVENTORYLOG_TABLE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String ITEM_TABLE = "ITEM_TABLE";
    public abstract InventoryLogDAO getInventoryLogDAO();

    /*private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized ((LOCK)){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DB_NAME).build();
                }
            }
        }
        return instance;
    }*/
}
