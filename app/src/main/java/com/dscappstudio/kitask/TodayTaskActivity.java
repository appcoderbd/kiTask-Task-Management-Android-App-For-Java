package com.dscappstudio.kitask;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.adapter.TaskAdapter;
import com.dscappstudio.kitask.adapter.TodayTaskAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;
import com.dscappstudio.kitask.oparetion.TodayTask;

import java.util.List;

public class TodayTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConstraintLayout task_add_btn;
    LinearLayout empty_box;
    RecyclerView taskRecycler;
    TodayTaskAdapter todayTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_task);

        toolbar = findViewById(R.id.toolbar);
        task_add_btn = findViewById(R.id.task_add_btn);
        empty_box = findViewById(R.id.empty_box);
        taskRecycler = findViewById(R.id.taskRecycler);





        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        task_add_btn.setOnClickListener(v -> {
            Intent intent = new Intent(TodayTaskActivity.this, TDTaskActivity.class);
            startActivity(intent);
        });

        // অ্যাডাপ্টার সেট করা
        todayTaskAdapter = new TodayTaskAdapter(this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskRecycler.setAdapter(todayTaskAdapter);

        // ডাটাবেস থেকে ডেটা আনতে LiveData ব্যবহার
        AppDatabase database = AppDatabase.getInstance(this);
        database.todayTaskDao().getAllTodayTask().observe(this, new Observer<List<TodayTask>>() {
            @Override
            public void onChanged(List<TodayTask> tasks) {
                if (tasks != null && !tasks.isEmpty()) {
                    todayTaskAdapter.todayTaskList(tasks);
                    empty_box.setVisibility(View.GONE); // Hide empty box if tasks exist
                } else {
                    empty_box.setVisibility(View.VISIBLE); // Show empty box if no tasks
                }
            }
        });

    }
}