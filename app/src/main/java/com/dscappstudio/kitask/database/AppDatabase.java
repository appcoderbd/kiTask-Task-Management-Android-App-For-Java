package com.dscappstudio.kitask.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.dscappstudio.kitask.oparetion.AppBlocked;
import com.dscappstudio.kitask.oparetion.AppBlockedDao;
import com.dscappstudio.kitask.oparetion.Routine;
import com.dscappstudio.kitask.oparetion.RoutineDao;
import com.dscappstudio.kitask.oparetion.Task;
import com.dscappstudio.kitask.oparetion.TaskDao;
import com.dscappstudio.kitask.oparetion.TaskTimeLog;
import com.dscappstudio.kitask.oparetion.TaskTimeLogDao;
import com.dscappstudio.kitask.oparetion.TodayTask;
import com.dscappstudio.kitask.oparetion.TodayTaskDao;
import com.dscappstudio.kitask.oparetion.TodoList;
import com.dscappstudio.kitask.oparetion.TodoListDao;
import com.dscappstudio.kitask.profile.Profile;
import com.dscappstudio.kitask.profile.ProfileDao;

@Database(entities = {
        Profile.class,
        Task.class,
        Routine.class,
        TaskTimeLog.class,
        TodayTask.class,
        TodoList.class,
        AppBlocked.class}, version = 6, exportSchema = false) // Version updated
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ProfileDao profileDao();
    public abstract TaskDao taskDao(); // Added TaskDao
    public abstract RoutineDao routineDao();
    public abstract TaskTimeLogDao taskTimeLogDao();
    public abstract TodayTaskDao todayTaskDao();
    public abstract TodoListDao todoListDao();
    public abstract AppBlockedDao appBlockedDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "user_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

