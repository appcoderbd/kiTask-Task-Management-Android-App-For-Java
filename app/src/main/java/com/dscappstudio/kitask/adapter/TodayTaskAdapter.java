package com.dscappstudio.kitask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.TodayTask;

import java.util.List;

public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.TodayTaskHolder> {

    private Context context;
    private List<TodayTask> todayTaskList;

    public TodayTaskAdapter(Context context){
        this.context = context;
    }

    public void todayTaskList (List<TodayTask> todayTaskList){
        if (todayTaskList != null){
            this.todayTaskList = todayTaskList;
        }
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public TodayTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_task_list_layout, parent, false);
        return new TodayTaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayTaskHolder holder, int position) {
        TodayTask todayTask = todayTaskList.get(position);
        holder.showTaskName.setText(todayTask.getTaskName());
        holder.priority.setText(todayTask.getTaskPriority());
        holder.start_date.setText(todayTask.getTaskDay());

        if (todayTask.getTaskStatus().equals("Pending")){
            holder.task_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.hourglass));
        }else {
            holder.task_status.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.complete));
        }

        holder.view_task.setOnClickListener(v -> {
            // সঠিক কনটেক্সট ব্যবহার করা হচ্ছে
            AppDatabase db = AppDatabase.getInstance(v.getContext());

            new Thread(() -> {
                // ID দিয়ে টাস্ক ডিলিট করা
                db.todayTaskDao().deleteTodayTaskById(todayTask.getId());

            }).start();

        });

    }

    @Override
    public int getItemCount() {
        return todayTaskList == null ? 0: todayTaskList.size();
    }

    public static class TodayTaskHolder extends RecyclerView.ViewHolder{

        TextView showTaskName, priority, start_date, duration;
        ImageView view_task, task_status;


        public TodayTaskHolder(@NonNull View itemView) {
            super(itemView);

            showTaskName = itemView.findViewById(R.id.showTaskName);
            priority = itemView.findViewById(R.id.priority);
            start_date = itemView.findViewById(R.id.start_date);
            duration = itemView.findViewById(R.id.duration);
            view_task = itemView.findViewById(R.id.view_task);
            task_status = itemView.findViewById(R.id.task_status);
        }
    }

}
