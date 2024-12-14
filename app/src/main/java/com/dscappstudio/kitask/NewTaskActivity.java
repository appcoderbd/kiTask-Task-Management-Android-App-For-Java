package com.dscappstudio.kitask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.adapter.TaskAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;

import java.util.List;


public class NewTaskActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConstraintLayout task_add_btn;
    RecyclerView taskRecycler;
    TaskAdapter taskAdapter;
    LinearLayout empty_box;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        toolbar = findViewById(R.id.toolbar);
        task_add_btn = findViewById(R.id.task_add_btn);
        taskRecycler = findViewById(R.id.taskRecycler);
        empty_box = findViewById(R.id.empty_box);


        // ToolBar ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        // ToolBar ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        task_add_btn.setOnClickListener(v -> {AddTaskAc();});

        // অ্যাডাপ্টার সেট করা
        taskAdapter = new TaskAdapter(this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        taskRecycler.setAdapter(taskAdapter);

        // ডাটাবেস থেকে ডেটা আনতে LiveData ব্যবহার
        AppDatabase database = AppDatabase.getInstance(NewTaskActivity.this);
        database.taskDao().getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                if (tasks != null && !tasks.isEmpty()) {
                    taskAdapter.setTaskList(tasks);
                    empty_box.setVisibility(View.GONE); // Hide empty box if tasks exist
                } else {
                    empty_box.setVisibility(View.VISIBLE); // Show empty box if no tasks
                }
            }
        });





    }

    // Move Add Task Activity~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void AddTaskAc(){

        Intent intent = new Intent(NewTaskActivity.this, AddTaskActivity.class);
        startActivity(intent);

        Log.i("Test1", "Nazim Uddin");
    }
}