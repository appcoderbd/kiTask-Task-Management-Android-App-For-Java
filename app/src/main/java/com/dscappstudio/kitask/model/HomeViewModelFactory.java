package com.dscappstudio.kitask.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dscappstudio.kitask.oparetion.RoutineDao;
import com.dscappstudio.kitask.oparetion.TaskDao;
import com.dscappstudio.kitask.oparetion.TodayTaskDao;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final TaskDao taskDao;
    private final TodayTaskDao todayTaskDao;
    private final RoutineDao routineDao;

    public HomeViewModelFactory(TaskDao taskDao, TodayTaskDao todayTaskDao, RoutineDao routineDao) {
        this.taskDao = taskDao;
        this.todayTaskDao = todayTaskDao;
        this.routineDao = routineDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(taskDao, todayTaskDao, routineDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}