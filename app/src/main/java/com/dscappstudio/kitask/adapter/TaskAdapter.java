package com.dscappstudio.kitask.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.AddTaskActivity;
import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.TaskDetailActivity;
import com.dscappstudio.kitask.TaskTimerActivity;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;
import com.dscappstudio.kitask.oparetion.TaskDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;

    public void setTaskList(List<Task> taskList) {
        if (taskList != null) {
            // পুরোনো Android ভার্সনের জন্য ফিল্টার করা
            List<Task> filteredList = new ArrayList<>();
            for (Task task : taskList) {
                if ("Pending".equals(task.getStatus()) || "Running".equals(task.getStatus())) {
                    filteredList.add(task);
                }
            }
            this.taskList = filteredList;
        } else {
            this.taskList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }


    public TaskAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.taskPriority.setText(task.getTaskPriority());
        holder.taskDuration.setText("Duration: "+task.getTaskDuration());
        holder.start_date.setText("Start: "+task.getTaskStartDate());

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.pi_card.setBackgroundColor(color);

        // প্রোগ্রেস ক্যালকুলেশন
        int progress = calculateProgress(task.getTaskStartDate(), task.getTaskDuration());

        // প্রোগ্রেস বার এবং টেক্সট আপডেট করা
        holder.task_progress.setProgress(progress);
        holder.progress_percentage.setText(progress + "%");

        holder.view_task.setOnClickListener(v -> {

            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(context); // Use the context
            View dialogView = inflater.inflate(R.layout.task_view_alert_dialog, null);

            // Initialize the views in the custom layout
            TextView routine_title = dialogView.findViewById(R.id.routine_title);
            LinearLayout i_delete = dialogView.findViewById(R.id.i_delete);
            LinearLayout i_view = dialogView.findViewById(R.id.i_view);
            LinearLayout i_cancel = dialogView.findViewById(R.id.i_cancel);
            LinearLayout i_start = dialogView.findViewById(R.id.i_start);


            // Build the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);
            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            // Set button click listener

            routine_title.setText(task.getTaskName());
            i_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            i_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // সঠিক কনটেক্সট ব্যবহার করা হচ্ছে
                    AppDatabase db = AppDatabase.getInstance(v.getContext());

                    new Thread(() -> {
                        // ID দিয়ে টাস্ক ডিলিট করা
                        db.taskDao().deleteTaskById(task.getId());
                        alertDialog.dismiss();      // ID ব্যবহার করে ডিলিট
                    }).start();
                }
            });

            i_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskTimerActivity.class);
                    intent.putExtra("taskId", task.getId());
                    context.startActivity(intent);

                }
            });

            i_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskDetailActivity.class);
                    intent.putExtra("taskId", task.getId());
                    context.startActivity(intent);
                }
            });

            // Show the dialog
            alertDialog.show();

            alertDialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width: Full screen
                    ViewGroup.LayoutParams.WRAP_CONTENT  // Height: Wrap content
            );

        });

        // **`task_status` বাটন ক্লিক হ্যান্ডলার**
        holder.task_status.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.task_status);
            popupMenu.getMenuInflater().inflate(R.menu.task_status_menu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                AppDatabase db = AppDatabase.getInstance(context);

                // টাস্কের স্ট্যাটাস পরিবর্তন করুন
                if (item.getItemId()==R.id.option_running){
                    task.setStatus("Running");
                }
                else if (item.getItemId()==R.id.option_complete){
                    task.setStatus("Complete");
                }
                // ডাটাবেসে আপডেট করুন
                new Thread(() -> {
                    TaskDao taskDao = db.taskDao();
                    taskDao.updateTask(task); // ডাটাবেসে স্ট্যাটাস আপডেট
                }).start();

                // ইউআই আপডেট করুন
                notifyItemChanged(position);
                return true;
            });

            popupMenu.show();
        });

        if (task.getStatus().equals("Running")) {
            holder.task_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.running));
        } else if (task.getStatus().equals("Complete")) {
            holder.task_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.complete));
        }
        else {
            holder.task_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.hourglass));
        }





    }

    private int calculateProgress(String taskStartDate, String taskDuration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        int progress = 0;

        try {
            Date startDate = dateFormat.parse(taskStartDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // ডিউরেশন থেকে দিন বের করা
            int durationInDays = convertDurationToDays(taskDuration);
            calendar.add(Calendar.DAY_OF_MONTH, durationInDays); // যোগ করা দিন
            Date endDate = calendar.getTime();

            Date currentDate = new Date();

            // মোট দিন বের করা
            long totalDuration = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            long elapsedDays = (currentDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

            // প্রোগ্রেস ক্যালকুলেট করা
            progress = (int) ((Math.max(0, elapsedDays) * 100) / totalDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return progress;
    }

    // ডিউরেশনকে মাস থেকে দিনে কনভার্ট করা
    private int convertDurationToDays(String taskDuration) {
        int days = 0;
        if (taskDuration != null && taskDuration.contains("Month")) {
            String durationStr = taskDuration.replaceAll("[^0-9]", ""); // শুধুমাত্র সংখ্যা নিয়ে আসা
            int months = Integer.parseInt(durationStr);
            days = months * 30; // গড় 1 মাস = 30 দিন
            Log.d("TaskDuration", "Duration in days: ");
        } else {
            // যদি ডিউরেশন মাসের বাইরে কিছু হয়, তাহলে নির্দিষ্ট দিন দিন
            days = 0;
        }
       // Log.d("TaskDuration", "Duration in days: " + days);
        return days;
    }


    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskPriority, taskDuration, start_date, progress_percentage;
        ImageView view_task, task_status;
        CardView pi_card;
        ProgressBar task_progress;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.showTaskName);
            taskPriority = itemView.findViewById(R.id.priority);

            taskDuration = itemView.findViewById(R.id.duration);
            start_date = itemView.findViewById(R.id.start_date);

            progress_percentage = itemView.findViewById(R.id.progress_percentage);
            task_progress = itemView.findViewById(R.id.task_progress);

            view_task = itemView.findViewById(R.id.view_task);
            task_status = itemView.findViewById(R.id.task_status);
            pi_card = itemView.findViewById(R.id.pi_card);
        }
    }
}
