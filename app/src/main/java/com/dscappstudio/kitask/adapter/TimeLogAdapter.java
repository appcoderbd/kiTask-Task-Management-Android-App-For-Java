package com.dscappstudio.kitask.adapter;

import static android.text.format.DateUtils.formatDateTime;

import static androidx.core.util.TimeUtils.formatDuration;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.oparetion.TaskTimeLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeLogAdapter extends RecyclerView.Adapter<TimeLogAdapter.ViewHolder> {

    private List<TaskTimeLog> timeLogs;

    public TimeLogAdapter(List<TaskTimeLog> timeLogs) {
        this.timeLogs = timeLogs;
    }

    public void updateData(List<TaskTimeLog> newLogs) {
        this.timeLogs.clear();
        this.timeLogs.addAll(newLogs);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_log, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskTimeLog log = timeLogs.get(position);

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
//        holder.startTime.setText("Start: " + sdf.format(new Date(log.getStartTime())));
//        holder.endTime.setText("End: " + sdf.format(new Date(log.getEndTime())));

        // Convert timestamp to readable format
        String startTime = formatDateTime(log.getStartTime());
        String endTime = formatDateTime(log.getEndTime());

        // Calculate total time
        long durationMillis = log.getEndTime() - log.getStartTime();
        String totalTime = formatDuration(durationMillis);

        // Bind data to views
        holder.startTime.setText("Start: " + startTime);
        holder.endTime.setText("End: " + endTime);
        holder.totalTime.setText("Total: " + totalTime);
    }

    @Override
    public int getItemCount() {
        return timeLogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView startTime, endTime,totalTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            totalTime = itemView.findViewById(R.id.totalTime);
        }
    }

    // Helper Method: Format timestamp to human-readable time
    private String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    // Helper Method: Format duration to "HH:mm:ss"
    private String formatDuration(long durationMillis) {
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long hours = durationMillis / (1000 * 60 * 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
