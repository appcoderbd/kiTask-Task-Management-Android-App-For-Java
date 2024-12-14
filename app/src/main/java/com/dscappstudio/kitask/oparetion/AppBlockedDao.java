package com.dscappstudio.kitask.oparetion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppBlockedDao {

    // 1. Insert a list of blocked apps
    @Insert
    void insertApp(List<AppBlocked> appBlockedList);

    // 2. Insert a single blocked app
    @Insert
    void insertApp(AppBlocked appBlocked);

    // 3. Update the block status of an app
    @Update
    void updateApp(AppBlocked appBlocked);

    // 4. Delete a single app from the block list
    @Delete
    void deleteApp(AppBlocked appBlocked);

    // 5. Retrieve all blocked apps
    @Query("SELECT * FROM app_blocked")
    LiveData<List<AppBlocked>> getAllBlockedApps();

    // 6. Retrieve a specific app by its package name
    @Query("SELECT * FROM app_blocked WHERE packageName = :packageName LIMIT 1")
    AppBlocked getAppByPackageName(String packageName);

    // 7. Delete all apps from the block list
    @Query("DELETE FROM app_blocked")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM app_blocked")
    int getBlockedAppsCount();

    @Query("SELECT * FROM app_blocked")
    List<AppBlocked> getAllBlockedAppsSync(); // সিঙ্ক্রোনাসভাবে ডেটা রিটার্ন করবে

}
