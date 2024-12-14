package com.dscappstudio.kitask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.profile.Profile;
import com.dscappstudio.kitask.profile.ProfileActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // Duration of splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            // Check if profile exists in Room Database
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(SplashActivity.this);
                // Get profile synchronously
                Profile profile = db.profileDao().getProfile(); // Retrieve the profile synchronously

                runOnUiThread(() -> {
                    Intent intent;
                    if (profile != null) {
                        // Profile exists, navigate to MainActivity
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        // Profile doesn't exist, navigate to ProfileActivity
                        intent = new Intent(SplashActivity.this, ProfileActivity.class);
                    }
                    startActivity(intent);
                    finish(); // Close SplashActivity
                });
            }).start();
        }, SPLASH_DISPLAY_LENGTH);
    }
}