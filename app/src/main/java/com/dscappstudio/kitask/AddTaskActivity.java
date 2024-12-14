package com.dscappstudio.kitask;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText task_name, task_category, task_start_date, task_duration, task_priority, task_description;
    TextView save_btn, cancel_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        toolbar = findViewById(R.id.toolbar);

        task_name = findViewById(R.id.task_name);
        task_category = findViewById(R.id.task_category);
        task_start_date = findViewById(R.id.task_start_date);
        task_duration = findViewById(R.id.task_duration);
        task_priority = findViewById(R.id.task_priority);
        task_description = findViewById(R.id.task_description);

        save_btn = findViewById(R.id.save_btn);
        cancel_btn = findViewById(R.id.cancel_btn);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        task_start_date.setOnClickListener(v -> {DatePiker();});
        // Save Button Click Listener
        save_btn.setOnClickListener(v -> saveTask());
        cancel_btn.setOnClickListener(v -> {onBackPressed();});


    }

    // Start Date Piker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void DatePiker() {
        // ক্যালেন্ডারের বর্তমান তারিখ
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog দেখানো
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // নির্বাচিত তারিখ
                    String selectedDate = selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear;

                    // এখানে তারিখ স্টোর করার পর
                    task_start_date.setText(selectedDate);

                    // যদি তারিখ পার্স করতে চান
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    try {
                        Date date = sdf.parse(selectedDate);
                        // যদি পার্সিং সফল হয়, তারিখ নিয়ে পরবর্তী কাজ করতে পারেন
                        Log.d("Selected Date", "Date: " + date.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Start Date Piker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Save Task to Database
    private void saveTask() {
        String name = task_name.getText().toString().trim();
        String category = task_category.getText().toString().trim();
        String startDate = task_start_date.getText().toString().trim();
        String duration = task_duration.getText().toString().trim();
        String priority = task_priority.getText().toString().trim();
        String description = task_description.getText().toString().trim();

        // Validation
        if (name.isEmpty() || category.isEmpty() || startDate.isEmpty() || duration.isEmpty() || priority.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Task object
        Task task = new Task();
        task.setTaskName(name);
        task.setTaskCategory(category);
        task.setTaskStartDate(startDate);
        task.setTaskDuration(duration);
        task.setTaskPriority(priority);
        task.setTaskDescription(description);
        task.setStatus("Pending");

        // Insert Task into Database
        AppDatabase db = AppDatabase.getInstance(this);
        new Thread(() -> {
            db.taskDao().insertTask(task);
            runOnUiThread(() -> {
                Toast.makeText(AddTaskActivity.this, "Task Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            });
        }).start();
    }
}