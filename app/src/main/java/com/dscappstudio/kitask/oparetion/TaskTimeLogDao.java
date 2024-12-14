package com.dscappstudio.kitask.oparetion;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskTimeLogDao {

    @Insert
    void insertTimeLog(TaskTimeLog timeLog);

    @Query("SELECT SUM(endTime - startTime) FROM task_time_logs WHERE taskId = :taskId")
    long getTotalTimeForTask(int taskId);

    @Query("SELECT * FROM task_time_logs WHERE taskId = :taskId ORDER BY startTime DESC")
    List<TaskTimeLog> getTimeLogsForTask(int taskId);


}
