package com.dscappstudio.kitask;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.adapter.TimeLogAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;
import com.dscappstudio.kitask.oparetion.TaskTimeLog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class TaskDetailActivity extends AppCompatActivity {


    private int taskId;
    private TextView taskTitle, taskDescription;
    private RecyclerView timeLogRecyclerView;
    private BarChart barChart;
    private TimeLogAdapter timeLogAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        // Initialize UI Components
        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDescription);
        timeLogRecyclerView = findViewById(R.id.timeLogRecyclerView);
        barChart = findViewById(R.id.barChart);
        toolbar = findViewById(R.id.toolbar);
        taskId = getIntent().getIntExtra("taskId", -1);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {

            getOnBackPressedDispatcher().onBackPressed();

        });

        // Initialize RecyclerView
        timeLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeLogAdapter = new TimeLogAdapter(new ArrayList<>()); // Empty list initially
        timeLogRecyclerView.setAdapter(timeLogAdapter);

        // Load Data
        loadTaskDetails();
        loadTaskTimeLogs();

    }


    private void loadTaskDetails() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Task task = db.taskDao().getTaskById(taskId);

            runOnUiThread(() -> {
                if (task != null) {
                    taskTitle.setText(task.getTaskName());
                    taskDescription.setText(task.getTaskDescription());
                }
            });
        }).start();
    }

    private void loadTaskTimeLogs() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<TaskTimeLog> timeLogs = db.taskTimeLogDao().getTimeLogsForTask(taskId);

            runOnUiThread(() -> {
                if (timeLogs != null) {
                    timeLogAdapter.updateData(timeLogs); // Update RecyclerView
                    setupWeeklyChart(timeLogs);         // Update Chart
                }
            });
        }).start();
    }

    private void setupWeeklyChart(List<TaskTimeLog> timeLogs) {
        List<BarEntry> entries = new ArrayList<>();

        // 7 দিনের কাজের হিসাব রাখতে
        Map<String, Float> dailyWorkHours = new HashMap<>();
        long currentTime = System.currentTimeMillis();
        long sevenDaysAgo = currentTime - (7 * 24 * 60 * 60 * 1000); // গত ৭ দিনের জন্য ফিল্টার

        for (TaskTimeLog log : timeLogs) {
            // শুধু গত ৭ দিনের কাজ যোগ করুন
            if (log.getStartTime() >= sevenDaysAgo && log.getEndTime() <= currentTime) {
                String date = formatDate(log.getStartTime());
                float hours = (float) (log.getEndTime() - log.getStartTime()) / (1000 * 60 * 60); // ঘণ্টায় রূপান্তর
                dailyWorkHours.put(date, dailyWorkHours.getOrDefault(date, 0f) + hours);
            }
        }

        // চার্টের জন্য ডেটা তৈরি
        int index = 0;
        for (Map.Entry<String, Float> entry : dailyWorkHours.entrySet()) {
            entries.add(new BarEntry(index++, entry.getValue())); // বার এন্ট্রি তৈরি
        }

        // বার ডেটাসেট এবং চার্ট সেটআপ
        BarDataSet dataSet = new BarDataSet(entries, "Daily Work Hours");
        dataSet.setColor(getResources().getColor(R.color.teal_200)); // বার কালার
        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false); // ডিসক্রিপশন বন্ধ
        barChart.getXAxis().setDrawGridLines(false); // গ্রিড লাইন সরানো
        barChart.invalidate(); // চার্ট রিফ্রেশ
    }


    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}