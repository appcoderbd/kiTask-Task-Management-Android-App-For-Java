package com.dscappstudio.kitask;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.fragment.EventFragment;
import com.dscappstudio.kitask.fragment.GraphFragment;
import com.dscappstudio.kitask.fragment.HomeFragment;
import com.dscappstudio.kitask.fragment.ProfileFragment;
import com.dscappstudio.kitask.fragment.TaskFragment;
import com.dscappstudio.kitask.profile.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private CircleImageView profile_image;
    BottomNavigationView bottomNavigationView;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        profile_image = findViewById(R.id.profile_image);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        HomeFragment();
        requestPermissions();


        // Load profile data from Room Database
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(MainActivity.this);
            Profile profile = db.profileDao().getProfile(); // Fetch profile data

            if (profile != null) {
                runOnUiThread(() -> {
                    // Load profile photo if exists
                    if (profile.getProfilePhotoUri() != null) {
                        profile_image.setImageURI(Uri.parse(profile.getProfilePhotoUri()));
                    }
                });
            }
        }).start();

        // Bottom Navigation View ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                HomeFragment();
            }
            else if (item.getItemId() == R.id.event) {
                EventFragment();
            }
            else if (item.getItemId() == R.id.graph) {
                GraphFragment();
            }
            else if (item.getItemId() == R.id.task_list) {
                TaskFragment();
            }
            else if (item.getItemId() == R.id.profile) {
                ProfileFragment();
            }
            return true;
        });

    }

    // All Fragment~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void HomeFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new HomeFragment());
        ft.commit();
    }

    private void EventFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new EventFragment());
        ft.commit();
    }

    private void GraphFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new GraphFragment());
        ft.commit();
    }

    private void TaskFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new TaskFragment());
        ft.commit();
    }

    private void ProfileFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new ProfileFragment());
        ft.commit();
    }
    // All Fragment~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }


}