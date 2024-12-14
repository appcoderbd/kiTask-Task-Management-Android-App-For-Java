package com.dscappstudio.kitask.oparetion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoutineDao {

    @Insert
    void insertRoutine(Routine routine);
    @Delete
    void deleteRoutine(Routine routine);
    @Update
    void updateRoutine(Routine routine);

    @Query("SELECT * FROM RoutineTable")
    LiveData<List<Routine>> getAllRoutine();

    @Query("DELETE FROM RoutineTable WHERE id = :routineId")
    void deleteRoutineById(int routineId);

    @Query("SELECT COUNT(*) FROM RoutineTable")
    LiveData<Integer> getRoutineCount();

    @Query("SELECT * FROM RoutineTable WHERE work_day = :currentDay AND (start_time >= :currentTime OR end_time >= :currentTime)")
    List<Routine> getRoutinesForToday(String currentDay, String currentTime);

    @Query("SELECT * FROM RoutineTable WHERE work_day = :today")
    LiveData<List<Routine>> getTodayRoutines(String today);


}
