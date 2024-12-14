package com.dscappstudio.kitask.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dscappstudio.kitask.oparetion.Routine;
import com.dscappstudio.kitask.oparetion.RoutineDao;
import com.dscappstudio.kitask.oparetion.Task;
import com.dscappstudio.kitask.oparetion.TaskDao;
import com.dscappstudio.kitask.oparetion.TodayTask;
import com.dscappstudio.kitask.oparetion.TodayTaskDao;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final TaskDao taskDao;
    private final TodayTaskDao todayTaskDao;
    private final RoutineDao routineDao;

    public HomeViewModel(TaskDao taskDao, TodayTaskDao todayTaskDao, RoutineDao routineDao) {
        this.taskDao = taskDao;
        this.todayTaskDao = todayTaskDao;
        this.routineDao = routineDao;
    }

    public LiveData<Integer> getCompleteTaskCount() {
        return taskDao.getCompleteTaskCount();
    }

    public LiveData<Integer> getPendingAndRunningTaskCount() {
        return taskDao.getPendingAndRunningTaskCount();
    }

    public LiveData<List<Task>> getRunningTasks() {
        return taskDao.getRunningTasks(); // আপনার DAO-তে `getRunningTasks` তৈরি করুন।
    }

    public LiveData<List<TodayTask>> getTodayTasks(String today) {
        return todayTaskDao.getTodayTasks(today);
    }

    public LiveData<List<Routine>> getTodayRoutines(String today) {
        return routineDao.getTodayRoutines(today);
    }
}

