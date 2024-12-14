package com.dscappstudio.kitask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.dscappstudio.kitask.oparetion.TodayTask;

public class TDTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText tTaskName,tt_description;
    AutoCompleteTextView work_day, ed_priority;
    TextView save_btn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdtask);

        toolbar = findViewById(R.id.toolbar);
        tTaskName = findViewById(R.id.tTaskName);
        tt_description = findViewById(R.id.tt_description);
        work_day = findViewById(R.id.work_day);
        ed_priority = findViewById(R.id.ed_priority);
        save_btn = findViewById(R.id.save_btn);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {onBackPressed();});


        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        // Work priority
        String[] priority ={"High","Medium","Low"};

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

        save_btn.setOnClickListener(v -> {saveTodayTask();});

    }


    private void saveTodayTask(){

        String taskName = tTaskName.getText().toString().trim();
        String taskDes = tt_description.getText().toString().trim();
        String taskDay = work_day.getText().toString().trim();
        String taskPriority = ed_priority.getText().toString().trim();

        if (taskName.isEmpty() || taskDes.isEmpty() || taskDay.isEmpty() || taskPriority.isEmpty()){
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }

        TodayTask todayTask = new TodayTask();
        todayTask.setTaskName(taskName);
        todayTask.setTaskDay(taskDay);
        todayTask.setTaskPriority(taskPriority);
        todayTask.setTaskDescription(taskDes);
        todayTask.setTaskStatus("Pending");

        AppDatabase database = AppDatabase.getInstance(this);
        new Thread(()->{
            database.todayTaskDao().InsertTodayTask(todayTask);
            runOnUiThread(()->{
                Toast.makeText(this, "Today Task Saved Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });

        }).start();

    }
}