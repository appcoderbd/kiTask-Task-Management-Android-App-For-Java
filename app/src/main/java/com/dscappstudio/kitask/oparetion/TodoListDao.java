package com.dscappstudio.kitask.oparetion;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoListDao {

    @Insert
    void insert(TodoList todoList);

    @Update
    void update(TodoList todoList);

    @Delete
    void delete(TodoList todoList);

    // Get all tasks ordered by dueDate
    @Query("SELECT * FROM todo_list ORDER BY dueDate ASC")
    LiveData<List<TodoList>> getAllList();

    // Get pending tasks (not completed)
    @Query("SELECT * FROM todo_list WHERE isCompleted = 0 ORDER BY dueDate ASC")
    LiveData<List<TodoList>> getPendingTasks();

    // Get completed tasks
    @Query("SELECT * FROM todo_list WHERE isCompleted = 1 ORDER BY dueDate ASC")
    LiveData<List<TodoList>> getCompletedTasks();

    // Count total tasks
    @Query("SELECT COUNT(*) FROM todo_list")
    int getTaskCount();

    // Delete a task by ID
    @Query("DELETE FROM todo_list WHERE id = :taskId")
    void deleteTaskById(int taskId);



}
