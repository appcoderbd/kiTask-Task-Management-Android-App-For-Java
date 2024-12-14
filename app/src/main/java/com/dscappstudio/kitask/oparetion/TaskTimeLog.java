package com.dscappstudio.kitask.oparetion;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_time_logs")
public class TaskTimeLog {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int taskId;
    private long startTime; // Timestamp in milliseconds
    private long endTime;   // Timestamp in milliseconds

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
