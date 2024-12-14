package com.dscappstudio.kitask;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.adapter.BlockedAdapter;
import com.dscappstudio.kitask.block_operation.AppBlocker;
import com.dscappstudio.kitask.block_operation.AppUsageStatsPermission;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.AppBlocked;

import java.util.ArrayList;
import java.util.List;

public class BlockedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BlockedAdapter blockedAdapter;
    private List<AppBlocked> appBlockedList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        appBlockedList = new ArrayList<>();
        blockedAdapter = new BlockedAdapter(this, appBlockedList);
        recyclerView.setAdapter(blockedAdapter);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            int count = db.appBlockedDao().getBlockedAppsCount();

            if (count == 0) {
                db.appBlockedDao().insertApp(getBlockedAppsList());
            }
//            else {
//                db.appBlockedDao().deleteAll();
//                db.appBlockedDao().insertApp(getBlockedAppsList());
//            }

            runOnUiThread(this::loadBlockedAppsAndBlock);
        }).start();

        // Usage Stats Permission চেক করুন
        if (!AppUsageStatsPermission.hasPermission(this)) {
            AppUsageStatsPermission.requestPermission(this);
        }
    }

    private List<AppBlocked> getBlockedAppsList() {
        List<AppBlocked> appList = new ArrayList<>();
        appList.add(new AppBlocked("Facebook", "com.facebook.katana", false));
        appList.add(new AppBlocked("Instagram", "com.instagram.android", false));
        appList.add(new AppBlocked("TikTok", "com.zhiliaoapp.musically", false));
        appList.add(new AppBlocked("YouTube", "com.google.android.youtube", false));
        appList.add(new AppBlocked("Facebook Messenger", "com.facebook.orca", false));
        appList.add(new AppBlocked("Snapchat", "com.snapchat.android", false));
        appList.add(new AppBlocked("Twitter (X)", "com.twitter.android", false));
        appList.add(new AppBlocked("WhatsApp", "com.whatsapp", false));
        appList.add(new AppBlocked("Telegram", "org.telegram.messenger", false));
        appList.add(new AppBlocked("Imo", "com.imo.android.imoim", false));
        appList.add(new AppBlocked("Viber", "com.viber.voip", false));
        appList.add(new AppBlocked("Netflix", "com.netflix.mediaclient", false));
        appList.add(new AppBlocked("Amazon Prime Video", "com.amazon.avod.thirdpartyclient", false));
        appList.add(new AppBlocked("Hotstar", "in.startv.hotstar", false));
        appList.add(new AppBlocked("PUBG Mobile", "com.tencent.ig", false));
        appList.add(new AppBlocked("Free Fire", "com.dts.freefireth", false));
        appList.add(new AppBlocked("Candy Crush Saga", "com.king.candycrushsaga", false));
        appList.add(new AppBlocked("Chrome Browser", "com.android.chrome", false));
        appList.add(new AppBlocked("Google Play Store", "com.android.vending", false));
        appList.add(new AppBlocked("Spotify", "com.spotify.music", false));
        // (অন্য অ্যাপ গুলো যোগ করুন)
        return appList;
    }

    private void loadBlockedAppsAndBlock() {
        // ডাটাবেস থেকে ব্লকড অ্যাপস লোড করুন
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            List<AppBlocked> appList = db.appBlockedDao().getAllBlockedAppsSync(); // সিঙ্ক্রোনাস কল

            // ডাটাবেস থেকে লোড করা অ্যাপ গুলোর ব্লক স্ট্যাটাস চেক করুন
            runOnUiThread(() -> {
                appBlockedList.clear();
                appBlockedList.addAll(appList);
                blockedAdapter.notifyDataSetChanged();

                // অ্যাপ গুলোর ব্লক স্ট্যাটাস চেক করে ব্লক করুন
                for (AppBlocked appBlocked : appList) {
                    if (appBlocked.isBlocked()) {
                        // ব্লকড অ্যাপ ব্লক করুন
                        AppBlocker.blockApp(this, appBlocked.getPackageName());
                    } else {
                        // ব্লকড না হলে আনব্লক করুন
                        AppBlocker.unblockApp(this, appBlocked.getPackageName());
                    }
                }
            });
        }).start();
    }
}
