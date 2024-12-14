package com.dscappstudio.kitask.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dscappstudio.kitask.MainActivity;
import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.database.AppDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone;
    CircleImageView profile_image;
    private Uri selectedPhotoUri;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final int PICK_BACKUP_FILE = 1;
    Button btnSave, btnRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        profile_image = findViewById(R.id.profile_image);
        btnSave = findViewById(R.id.btnSave);
        btnRestore = findViewById(R.id.btnRestore);

        profile_image.setOnClickListener(v -> selectPhoto());
        btnSave.setOnClickListener(v -> saveProfile());
      //  btnRestore.setOnClickListener(v -> openFilePicker());  // Link the restore button to open the file picker

        // Check for permissions on devices running Android M and above
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            // Permission already granted, proceed with image selection
            proceedWithImageSelection();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with image selection
                proceedWithImageSelection();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Permission denied. Cannot access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("IntentReset")
    private void proceedWithImageSelection() {
        // Code for selecting or accessing images
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 (API Level 29) and above, using scoped storage
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            startActivityForResult(intent, 1);
        } else {
            // For below Android 10, use MediaStore to pick an image
            @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }
    }

    private void selectPhoto() {
        // Open the image picker when the profile image is clicked
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void saveProfile() {
        // Get data from the EditText fields
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validate the input
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || selectedPhotoUri == null) {
            Toast.makeText(this, "Please fill in all fields and select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Profile object
        Profile profile = new Profile(name, email, phone, selectedPhotoUri.toString());

        // Save the profile in the Room database
        AppDatabase db = AppDatabase.getInstance(this);
        new Thread(() -> {
            db.profileDao().insertProfile(profile);
            runOnUiThread(() -> Toast.makeText(this, "Profile saved successfully!", Toast.LENGTH_SHORT).show());

            // Go to the MainActivity after saving the profile
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }).start();
    }

    // Open file picker to restore the profile
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_BACKUP_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                // Handle photo selection
                selectedPhotoUri = data.getData();
                profile_image.setImageURI(selectedPhotoUri);
            } else if (requestCode == PICK_BACKUP_FILE) {
                // Handle backup file restore
                Uri backupUri = data.getData();
                if (backupUri != null) {
                    restoreDatabase(backupUri);  // Call to restore the database
                }
            }
        }
    }

    // Restore the database from backup
    private void restoreDatabase(Uri backupUri) {
        try {
            String dbPath = getDatabasePath("user_database").getAbsolutePath();

            // Open input stream from backup Uri
            InputStream inputStream = getContentResolver().openInputStream(backupUri);
            OutputStream outputStream = new FileOutputStream(dbPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            Toast.makeText(this, "Profile restored successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to restore profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
