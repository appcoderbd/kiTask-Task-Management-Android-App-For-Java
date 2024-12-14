package com.dscappstudio.kitask;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Routine;

import java.util.Locale;

public class AddRoutineActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText routine_type, start_time, end_time, routine_work;
    AutoCompleteTextView work_day, ed_priority;
    // Days of the week
    TextView save_btn, cancel_btn;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);


        toolbar = findViewById(R.id.toolbar);
        routine_type = findViewById(R.id.routine_type);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        routine_work = findViewById(R.id.routine_work);
        save_btn = findViewById(R.id.save_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        work_day = findViewById(R.id.work_day);
        ed_priority = findViewById(R.id.ed_priority);

        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        // Work priority
        String[] priority ={"High","Medium","Low"};

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {onBackPressed();});

        ArrayAdapter<String> day_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, days);
        work_day.setAdapter(day_adapter);
        work_day.setThreshold(0);

        ArrayAdapter<String> priority_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, priority);
        ed_priority.setAdapter(priority_adapter);
        ed_priority.setThreshold(0);

        // Ensure dropdown appears on touch
        work_day.setOnTouchListener((v, event) -> {
            work_day.showDropDown();
            return false;
        });

        ed_priority.setOnTouchListener((v, event) -> {
            ed_priority.showDropDown();
            return false;
        });

        start_time.setOnClickListener(v -> {StartTime();});
        end_time.setOnClickListener(v -> {EndTime();});
        cancel_btn.setOnClickListener(v -> {onBackPressed();});
        save_btn.setOnClickListener(v -> {submitData();});









    }


    // Start Time~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void StartTime(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddRoutineActivity.this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    // Format time and set it in EditText
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    start_time.setText(formattedTime);
                },
                12, // Default hour (12 PM)
                0, // Default minute
                true // Use 24-hour format (set false for AM/PM format)
        );
        timePickerDialog.show();
    }
    // Start Time~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    // Start Time~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void EndTime(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddRoutineActivity.this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    // Format time and set it in EditText
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    end_time.setText(formattedTime);
                },
                12, // Default hour (12 PM)
                0, // Default minute
                true // Use 24-hour format (set false for AM/PM format)
        );
        timePickerDialog.show();
    }
    // Start Time~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // Submit Data in Room Database~~~~~~~~~~~~
    private void submitData() {
        String routineType = routine_type.getText().toString().trim();
        String startTime = start_time.getText().toString().trim();
        String endTime = end_time.getText().toString().trim();
        String routineWork = routine_work.getText().toString().trim();
        String day = work_day.getText().toString().trim();
        String priority = ed_priority.getText().toString().trim();

        if (routineType.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || routineWork.isEmpty() || day.isEmpty() || priority.isEmpty()){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_LONG).show();
            return;
        }

        // Add Data~~~~~~~~~~~~~
        Routine routine = new Routine();
        routine.setRoutineName(routineType);
        routine.setStartTime(startTime);
        routine.setEndTime(endTime);
        routine.setWorkDay(day);
        routine.setWorkPriority(priority);

        // Database
        AppDatabase database = AppDatabase.getInstance(this);
        new Thread(() -> {
            database.routineDao().insertRoutine(routine);
            runOnUiThread(() -> {
                Toast.makeText(AddRoutineActivity.this, "Routine Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

    // Notification Setup~~~~~~~~~~~~~~~~~~~~~~




    // Notification Setup~~~~~~~~~~~~~~~~~~~~~~
}