package com.dscappstudio.kitask.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dscappstudio.kitask.BlockedActivity;
import com.dscappstudio.kitask.MainActivity;
import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.profile.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    CircleImageView profile_image;
    TextView user_name, user_email;
    ConstraintLayout edit_profile_layout, pin_set_layout, policy_layout, others_layout, database_backup_layout, faq_layout, logout_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_image = profileView.findViewById(R.id.profile_image);
        user_name = profileView.findViewById(R.id.user_name);
        user_email = profileView.findViewById(R.id.user_email);

        logout_layout = profileView.findViewById(R.id.logout_layout);
        database_backup_layout = profileView.findViewById(R.id.database_backup_layout);
        faq_layout = profileView.findViewById(R.id.faq_layout);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getActivity());

            // Fetch profile data
            Profile profile = db.profileDao().getProfile();

            if (profile != null) {
                if (getActivity() != null) { // Check activity before UI updates
                    getActivity().runOnUiThread(() -> {
                        if (getActivity() == null) return; // Secondary null check for safety

                        // Update UI with profile data
                        user_name.setText(profile.getName());
                        user_email.setText(profile.getEmail());

                        // Load profile photo if exists
                        if (profile.getProfilePhotoUri() != null) {
                            profile_image.setImageURI(Uri.parse(profile.getProfilePhotoUri()));
                        }
                    });
                }
            }
        }).start();

        logout_layout.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finishAndRemoveTask(); // অ্যাপ বন্ধ করে এবং Recents থেকে সরিয়ে দেয়
            }
        });

        database_backup_layout.setOnClickListener(v -> {backupDatabaseToDownloads();});

        faq_layout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BlockedActivity.class);
            startActivity(intent);
        });




        return profileView;
    }

    // Database Backup file-------------------------------------------------------------------------
    public void backupDatabaseToDownloads() {
        String dbPath = requireContext().getDatabasePath("user_database").getAbsolutePath();

        try {
            FileInputStream fis = new FileInputStream(dbPath);

            // ContentValues ব্যবহার করে ডাউনলোড ফোল্ডারে সেভ করা
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "user_database_backup.db");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/octet-stream");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/kiTaskBackup");

            Uri uri = requireContext().getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            if (uri == null) {
                Toast.makeText(requireContext(), "Failed to create backup file", Toast.LENGTH_LONG).show();
                return;
            }

            OutputStream fos = requireContext().getContentResolver().openOutputStream(uri);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            fis.close();
            fos.close();

            Toast.makeText(requireContext(), "Backup created in Downloads folder", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to create backup: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



}