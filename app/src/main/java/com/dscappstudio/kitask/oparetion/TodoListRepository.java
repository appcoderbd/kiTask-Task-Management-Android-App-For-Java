package com.dscappstudio.kitask.oparetion;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dscappstudio.kitask.database.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoListRepository {

    private TodoListDao todoListDao;
    private LiveData<List<TodoList>> allTodoList;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(); // Single thread executor

    public TodoListRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        todoListDao = database.todoListDao();
        allTodoList = todoListDao.getAllList();
    }

    // Insert Task
    public void insert(TodoList todoList) {
        executorService.execute(() -> todoListDao.insert(todoList));
    }

    // Update Task
    public void update(TodoList todoList) {
        executorService.execute(() -> todoListDao.update(todoList));
    }

    // Delete Task
    public void delete(TodoList todoList) {
        executorService.execute(() -> todoListDao.delete(todoList));
    }

    // Get All Tasks
    public LiveData<List<TodoList>> getAllTodoList() {
        return allTodoList;
    }
}

