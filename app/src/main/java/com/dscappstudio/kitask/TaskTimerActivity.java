package com.dscappstudio.kitask;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.TaskTimeLog;
import com.dscappstudio.kitask.oparetion.TaskTimeLogDao;

public class TaskTimerActivity extends AppCompatActivity {

    private int taskId; // Task ID passed from intent
    private long startTime;
    private boolean isRunning = false;

    Toolbar toolbar;
    TextView timerTextView;
    Button stopButton;

    private Handler handler = new Handler();
    private TaskTimeLogDao timeLogDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_timer);


        toolbar = findViewById(R.id.toolbar);
        timerTextView = findViewById(R.id.timerTextView);
        stopButton = findViewById(R.id.stopButton);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        timeLogDao = db.taskTimeLogDao();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        taskId = getIntent().getIntExtra("taskId", -1);

        startTimer();

        stopButton.setOnClickListener(v -> stopTimer());





    }

    private void startTimer() {
        isRunning = true;
        startTime = System.currentTimeMillis();
        handler.post(updateTimer);
    }

    private void stopTimer() {
        if (isRunning) {
            isRunning = false;
            long endTime = System.currentTimeMillis();

            // Stop the handler and update UI
            handler.removeCallbacks(updateTimer);
            stopButton.setText("Stopped");

            // Save the time log to database
            new Thread(() -> {
                TaskTimeLog log = new TaskTimeLog();
                log.setTaskId(taskId);
                log.setStartTime(startTime);
                log.setEndTime(endTime);
                timeLogDao.insertTimeLog(log);
            }).start();
        }
    }

    private final Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long elapsed = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsed / 1000) % 60;
                int minutes = (int) (elapsed / (1000 * 60)) % 60;
                int hours = (int) (elapsed / (1000 * 60 * 60));

                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timerTextView.setText(time);

                handler.postDelayed(this, 1000);
            }
        }
    };
}