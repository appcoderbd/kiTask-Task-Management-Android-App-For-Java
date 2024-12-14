package com.dscappstudio.kitask.block_operation;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.dscappstudio.kitask.BlockedActivity;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.AppBlocked;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName().toString();

            // ডাটাবেসে অ্যাপ ব্লক স্ট্যাটাস চেক করুন
            AppDatabase db = AppDatabase.getInstance(this);
            AppBlocked appBlocked = db.appBlockedDao().getAppByPackageName(packageName);

            if (appBlocked != null && appBlocked.isBlocked()) {
                // যদি অ্যাপ ব্লকড থাকে তবে BlockedActivity খুলুন
                Intent intent = new Intent(this, BlockedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onInterrupt() {
        // এখানে কোনো কার্যক্রম নেই
    }
}
