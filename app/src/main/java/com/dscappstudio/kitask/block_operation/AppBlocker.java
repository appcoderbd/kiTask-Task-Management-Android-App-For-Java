package com.dscappstudio.kitask.block_operation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class AppBlocker {

    // অ্যাপ ব্লক করার কোড
    public static void blockApp(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            int currentState = pm.getApplicationEnabledSetting(packageName);

            // যদি অ্যাপ ইতিমধ্যে ব্লক না থাকে, তবে ব্লক করুন
            if (currentState != PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                pm.setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
                Log.d("AppBlocker", "App Blocked: " + packageName);
            } else {
                Log.d("AppBlocker", "App is already blocked: " + packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AppBlocker", "Error Blocking App: " + e.getMessage());
        }
    }

    public static void unblockApp(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            int currentState = pm.getApplicationEnabledSetting(packageName);

            // যদি অ্যাপ ইতিমধ্যে আনব্লক না থাকে, তবে আনব্লক করুন
            if (currentState != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                pm.setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
                Log.d("AppBlocker", "App Unblocked: " + packageName);
            } else {
                Log.d("AppBlocker", "App is already unblocked: " + packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AppBlocker", "Error Unblocking App: " + e.getMessage());
        }
    }
}
