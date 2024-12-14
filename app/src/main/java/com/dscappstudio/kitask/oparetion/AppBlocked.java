package com.dscappstudio.kitask.oparetion;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "app_blocked")
public class AppBlocked {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String appName;      // অ্যাপের নাম
    private String packageName;  // অ্যাপের প্যাকেজ নাম
    private boolean isBlocked;   // ব্লকের অবস্থা (ON/OFF)

    // Constructor
    public AppBlocked(String appName, String packageName, boolean isBlocked) {
        this.appName = appName;
        this.packageName = packageName;
        this.isBlocked = isBlocked;
    }

    // Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
