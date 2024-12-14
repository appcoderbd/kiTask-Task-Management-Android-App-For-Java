package com.dscappstudio.kitask.oparetion;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodayTaskDao {

    @Insert
    void InsertTodayTask(TodayTask todayTask);
    @Update
    void UpdateTodayTask(TodayTask todayTask);
    @Delete
    void DeleteTodayTask(TodayTask todayTask);

    @Query("SELECT * FROM TodayTaskTable")
    LiveData<List<TodayTask>> getAllTodayTask();


    @Query("DELETE FROM TodayTaskTable WHERE id = :todayTaskId")
    void deleteTodayTaskById(int todayTaskId);

    @Query("SELECT COUNT(*) FROM TodayTaskTable")
    LiveData<Integer> getToadyTaskCount();

    @Query("SELECT * FROM TodayTaskTable WHERE task_day = :today")
    LiveData<List<TodayTask>> getTodayTasks(String today);


}
