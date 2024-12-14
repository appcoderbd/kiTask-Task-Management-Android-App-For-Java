package com.dscappstudio.kitask.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskCompleteAdapter extends RecyclerView.Adapter<TaskCompleteAdapter.TaskCompleteHolder> {

    private List<Task> taskList;
    private Context context;

    public TaskCompleteAdapter(Context context) {
        this.context = context;
    }

    public void setCompleteTaskList(List<Task> allTasks) {
        this.taskList = new ArrayList<>();
        for (Task task : allTasks) {
            if ("Complete".equals(task.getStatus())) {
                this.taskList.add(task);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskCompleteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_complete_item_layout, parent, false);
        return new TaskCompleteHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskCompleteHolder holder, int position) {
        Task task = taskList.get(position);
        holder.showTaskName.setText(task.getTaskName());
        holder.priority.setText(task.getTaskPriority());
        holder.start_date.setText(task.getTaskStartDate());
        holder.duration.setText(task.getTaskDuration());

        // টোটাল সময় লোড করা
        new Thread(() -> {
            // TaskTimeLogDao থেকে ডেটা ফেচ করা
            AppDatabase db = AppDatabase.getInstance(context);
            long totalTimeMillis = db.taskTimeLogDao().getTotalTimeForTask(task.getId());

            // সময়কে ঘণ্টা ও মিনিটে কনভার্ট করা
            long totalHours = totalTimeMillis / (1000 * 60 * 60);
            long totalMinutes = (totalTimeMillis / (1000 * 60)) % 60;

            // UI-তে সেট করার জন্য মূল থ্রেডে ফিরতে হবে
            ((RecyclerView.ViewHolder) holder).itemView.post(() -> {
                holder.total_hour.setText(totalHours + "h " + totalMinutes + "m");
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public static class TaskCompleteHolder extends RecyclerView.ViewHolder {
        TextView showTaskName, priority, start_date,duration,total_hour;

        public TaskCompleteHolder(@NonNull View itemView) {
            super(itemView);
            showTaskName = itemView.findViewById(R.id.showTaskName);
            priority = itemView.findViewById(R.id.priority);
            start_date = itemView.findViewById(R.id.start_date);
            duration = itemView.findViewById(R.id.duration);

            total_hour = itemView.findViewById(R.id.total_hour);
        }
    }
}
