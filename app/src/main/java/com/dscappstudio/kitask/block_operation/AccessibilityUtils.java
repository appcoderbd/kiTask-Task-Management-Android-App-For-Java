package com.dscappstudio.kitask.block_operation;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.dscappstudio.kitask.R;

import java.util.ArrayList;
import java.util.List;

public class AccessibilityUtils {

    // অ্যাপ ব্লক করার মেথড
    public static void blockApp(Context context, String packageName) {
        // এখানে অ্যাপ ব্লক করার লজিক লিখুন (যেমন অ্যাক্সেসিবিলিটি সার্ভিস দিয়ে ব্লক করা)

        // Toast শো করার জন্য মেইন থ্রেডে কল করতে হবে
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(context, "Blocking app: " + packageName, Toast.LENGTH_SHORT).show();
            });
        }
    }

    public static void unblockApp(Context context, String packageName) {
        // এখানে অ্যাপ আনব্লক করার লজিক লিখুন

        // Toast শো করার জন্য মেইন থ্রেডে কল করতে হবে
        if (context != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(context, "Unblocking app: " + packageName, Toast.LENGTH_SHORT).show();
            });
        }
    }


    /**
     * Check if the accessibility service is enabled
     *
     * @param context Context of the application
     * @param service The name of the service (e.g., MyAccessibilityService)
     * @return true if enabled, false otherwise
     */
    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> service) {
        String colonSeparatedColon = ":";
        String colon = colonSeparatedColon + service.getName();
        int colonIndex = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                .indexOf(colon);

        return colonIndex > -1;
    }

    /**
     * Get a list of blocked app package names
     *
     * @return List of blocked package names
     */
    public static List<String> getBlockedApps() {
        List<String> blockedApps = new ArrayList<>();
        blockedApps.add("com.facebook.katana");
        blockedApps.add("com.instagram.android");
        blockedApps.add("com.zhiliaoapp.musically");
        blockedApps.add("com.google.android.youtube");
        blockedApps.add("com.facebook.orca");
        blockedApps.add("com.snapchat.android");
        blockedApps.add("com.twitter.android");
        blockedApps.add("com.whatsapp");
        blockedApps.add("org.telegram.messenger");
        blockedApps.add("com.imo.android.imoim");
        blockedApps.add("com.viber.voip");
        blockedApps.add("com.netflix.mediaclient");
        blockedApps.add("com.amazon.avod.thirdpartyclient");
        blockedApps.add("in.startv.hotstar");
        blockedApps.add("com.tencent.ig");
        blockedApps.add("com.dts.freefireth");
        blockedApps.add("com.king.candycrushsaga");
        blockedApps.add("com.android.chrome");
        blockedApps.add("com.android.vending");
        blockedApps.add("com.spotify.music");
        return blockedApps;
    }

    /**
     * Check if the given package name should be blocked
     *
     * @param packageName Package name to check
     * @return true if the app is blocked, false otherwise
     */
    public static boolean shouldBlockApp(String packageName) {
        List<String> blockedApps = getBlockedApps();
        return blockedApps.contains(packageName);
    }

    /**
     * Show a toast message to the user
     *
     * @param context Context of the application
     * @param message The message to display
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle the accessibility event to block apps
     *
     * @param event The accessibility event
     * @return true if the app is blocked, false otherwise
     */
    public static boolean handleAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();

        // Check if the app is in the blocked list
        if (shouldBlockApp(packageName)) {
            // Block the app
            return true;
        }

        return false;
    }

    /**
     * Show a custom blocked message for the app
     *
     * @param context Context of the application
     */
    public static void showBlockedMessage(Context context) {
        showToast(context, context.getString(R.string.app_blocked_message));
    }

}
