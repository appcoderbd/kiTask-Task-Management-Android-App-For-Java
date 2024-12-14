package com.dscappstudio.kitask.oparetion;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "RoutineTable")
public class Routine {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "routine_name")
    private String routineName;

    @ColumnInfo(name = "work_name")
    private String workName;

    @ColumnInfo(name = "start_time")
    private String startTime;

    @ColumnInfo(name = "end_time")
    private String endTime;

    @ColumnInfo(name = "work_day")
    private String workDay;

    @ColumnInfo(name = "work_priority")
    private String workPriority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getWorkPriority() {
        return workPriority;
    }

    public void setWorkPriority(String workPriority) {
        this.workPriority = workPriority;
    }
}
