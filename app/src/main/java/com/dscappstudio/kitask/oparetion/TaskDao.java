package com.dscappstudio.kitask.oparetion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM TaskTable")
    // List<Task> getAllTasks();
    LiveData<List<Task>> getAllTasks();

    @Query("UPDATE TaskTable SET status = :status WHERE id = :taskId")
    void updateTaskStatus(int taskId, String status);

    @Query("SELECT * FROM TaskTable WHERE id = :taskId LIMIT 1")
    Task getTaskById(int taskId);



    @Query("DELETE FROM TaskTable WHERE id = :taskId")
    void deleteTaskById(int taskId);

    // টাস্কের মোট সংখ্যা খুঁজে বের করার জন্য
    @Query("SELECT COUNT(*) FROM TaskTable")
    LiveData<Integer> getTaskCount();

    // Complete স্ট্যাটাস গুনবে
    @Query("SELECT COUNT(*) FROM TaskTable WHERE status = 'Complete'")
    LiveData<Integer> getCompleteTaskCount();

    // Pending এবং Running স্ট্যাটাস গুনবে
    @Query("SELECT COUNT(*) FROM TaskTable WHERE status = 'Pending' OR status = 'Running'")
    LiveData<Integer> getPendingAndRunningTaskCount();

    @Query("SELECT * FROM TaskTable WHERE status = 'Running'")
    LiveData<List<Task>> getRunningTasks();
}

