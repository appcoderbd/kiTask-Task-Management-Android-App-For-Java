package com.dscappstudio.kitask;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.adapter.TaskCompleteAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;

import java.util.List;

public class CompleteTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView complete_recycler;
    TaskCompleteAdapter taskCompleteAdapter;
    LinearLayout empty_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);


        toolbar = findViewById(R.id.toolbar);
        complete_recycler = findViewById(R.id.complete_recycler);
        empty_box = findViewById(R.id.empty_box);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        taskCompleteAdapter = new TaskCompleteAdapter(this);
        complete_recycler.setLayoutManager(new LinearLayoutManager(this));
        complete_recycler.setAdapter(taskCompleteAdapter);


        AppDatabase database = AppDatabase.getInstance(this);
        database.taskDao().getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                if (tasks != null && !tasks.isEmpty()) {
                    taskCompleteAdapter.setCompleteTaskList(tasks);
                    empty_box.setVisibility(View.GONE); // Hide empty box if tasks exist
                } else {
                    empty_box.setVisibility(View.VISIBLE); // Show empty box if no tasks
                }

            }
        });

    }
}