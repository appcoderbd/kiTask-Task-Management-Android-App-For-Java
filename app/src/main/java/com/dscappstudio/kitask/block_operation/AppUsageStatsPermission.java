package com.dscappstudio.kitask.block_operation;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

public class AppUsageStatsPermission {

    public static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String packageName = context.getApplicationContext().getPackageName();
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName);
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    public static void requestPermission(Context context) {
        if (!hasPermission(context)) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            context.startActivity(intent);
        }
    }
}
