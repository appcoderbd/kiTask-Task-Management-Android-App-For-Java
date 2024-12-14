package com.dscappstudio.kitask.model;

public class AppBlockedModel {

    private String appName;
    private String packageName;
    private boolean isBlocked;

    public AppBlockedModel (String appName, String packageName, boolean isBlocked) {
        this.appName = appName;
        this.packageName = packageName;
        this.isBlocked = isBlocked;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
