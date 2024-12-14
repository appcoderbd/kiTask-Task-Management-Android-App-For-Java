package com.dscappstudio.kitask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import com.dscappstudio.kitask.adapter.RoutineAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Routine;
import com.dscappstudio.kitask.oparetion.RoutineDao;

import java.util.List;
import java.util.Objects;

public class RoutineActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConstraintLayout task_add_btn;
    RecyclerView showDataRecycler;
    RoutineAdapter adapter;
    LinearLayout empty_box;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);


        toolbar = findViewById(R.id.toolbar);
        task_add_btn = findViewById(R.id.task_add_btn);
        showDataRecycler = findViewById(R.id.showDataRecycler);
        empty_box = findViewById(R.id.empty_box);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        task_add_btn.setOnClickListener(v -> {goRoutineAddActivity();});

        adapter = new RoutineAdapter(this);
        showDataRecycler.setLayoutManager(new LinearLayoutManager(this));
        showDataRecycler.setAdapter(adapter);


        AppDatabase database = AppDatabase.getInstance(this);
        database.routineDao().getAllRoutine().observe(this, new Observer<List<Routine>>() {
            @Override
            public void onChanged(List<Routine> routines) {
                if (routines != null && !routines.isEmpty()) {
                    adapter.setRoutineList(routines);
                    empty_box.setVisibility(View.GONE); // Hide empty box if tasks exist
                } else {
                    empty_box.setVisibility(View.VISIBLE); // Show empty box if no tasks
                }
            }
        });
        



    }


    // add routine activity~~~~~~~~~~~~~~~~~~~~
    private void goRoutineAddActivity(){
        Intent intent = new Intent(RoutineActivity.this, AddRoutineActivity.class);
        startActivity(intent);
    }
    // add routine activity~~~~~~~~~~~~~~~~~~~~



}