package com.dscappstudio.kitask.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.TaskDetailActivity;
import com.dscappstudio.kitask.TaskTimerActivity;
import com.dscappstudio.kitask.oparetion.Routine;
import com.dscappstudio.kitask.oparetion.Task;
import com.dscappstudio.kitask.oparetion.TodayTask;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TASK = 1;
    private static final int VIEW_TYPE_TODAY_TASK = 2;
    private static final int VIEW_TYPE_ROUTINE = 3;

    private List<Object> items = new ArrayList<>();

    public void setItems(List<Object> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    // Add this getter for retrieving current items
    public List<Object> getItems() {
        return new ArrayList<>(items); // Return a copy of the list to avoid direct modification
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Task) {
            return VIEW_TYPE_TASK;
        } else if (item instanceof TodayTask) {
            return VIEW_TYPE_TODAY_TASK;
        } else if (item instanceof Routine) {
            return VIEW_TYPE_ROUTINE;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TASK) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_layout, parent, false);
            return new TaskViewHolder(view);
        } else if (viewType == VIEW_TYPE_TODAY_TASK) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_task_list_layout, parent, false);
            return new TodayTaskViewHolder(view);
        } else if (viewType == VIEW_TYPE_ROUTINE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_item_list, parent, false);
            return new RoutineViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid view type");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof TaskViewHolder) {
            ((TaskViewHolder) holder).bind((Task) item);
        } else if (holder instanceof TodayTaskViewHolder) {
            ((TodayTaskViewHolder) holder).bind((TodayTask) item);
        } else if (holder instanceof RoutineViewHolder) {
            ((RoutineViewHolder) holder).bind((Routine) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView showTaskName, priority, start_date, duration;
        private ImageView task_status, view_task;
        private Context context;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            showTaskName = itemView.findViewById(R.id.showTaskName);
            priority = itemView.findViewById(R.id.priority);
            start_date = itemView.findViewById(R.id.start_date);
            duration = itemView.findViewById(R.id.duration);
            task_status = itemView.findViewById(R.id.task_status);
            view_task = itemView.findViewById(R.id.view_task);
            context = itemView.getContext();

        }

        public void bind(Task task) {
            showTaskName.setText(task.getTaskName());
            priority.setText(task.getTaskPriority());
            start_date.setText(task.getTaskStartDate());
            duration.setText(task.getTaskDuration());
            task_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.running));

            view_task.setOnClickListener(v -> {
                // Create an intent to navigate to NewTaskActivity
                Intent intent = new Intent(context, TaskDetailActivity.class);

                // Optionally, you can pass data with the intent (e.g., task details)
                intent.putExtra("taskId", task.getId());  // Pass task ID or other data if needed

                // Start the activity
                context.startActivity(intent);
            });

            task_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaskTimerActivity.class);
                    intent.putExtra("taskId", task.getId());
                    context.startActivity(intent);

                }
            });

        }
    }

    static class TodayTaskViewHolder extends RecyclerView.ViewHolder {
        private TextView showTaskName, priority, start_date, duration;

        public TodayTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            showTaskName = itemView.findViewById(R.id.showTaskName);
            priority = itemView.findViewById(R.id.priority);
            start_date = itemView.findViewById(R.id.start_date);
            duration = itemView.findViewById(R.id.duration);
        }

        public void bind(TodayTask todayTask) {
            showTaskName.setText(todayTask.getTaskName());
            priority.setText(todayTask.getTaskPriority());
            start_date.setText(todayTask.getTaskDay());
            duration.setText(todayTask.getTaskStatus());

        }
    }

    static class RoutineViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_type_name, tv_start_time, tv_end_time, tv_day, tv_priority, tv_work;

        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type_name = itemView.findViewById(R.id.tv_type_name);
            tv_start_time = itemView.findViewById(R.id.tv_start_time);
            tv_end_time = itemView.findViewById(R.id.tv_end_time);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_priority = itemView.findViewById(R.id.tv_priority);
            tv_work = itemView.findViewById(R.id.tv_work);
        }

        public void bind(Routine routine) {
            tv_type_name.setText(routine.getRoutineName());
            tv_start_time.setText(routine.getStartTime());
            tv_end_time.setText(routine.getEndTime());
            tv_day.setText(routine.getWorkDay());
            tv_priority.setText(routine.getWorkPriority());
            tv_work.setText(routine.getWorkName());
        }
    }
}
