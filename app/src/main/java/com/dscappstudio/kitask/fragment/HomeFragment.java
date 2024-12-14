package com.dscappstudio.kitask.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dscappstudio.kitask.CompleteTaskActivity;
import com.dscappstudio.kitask.MainActivity;
import com.dscappstudio.kitask.NewTaskActivity;
import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.RoutineActivity;
import com.dscappstudio.kitask.TodayTaskActivity;
import com.dscappstudio.kitask.adapter.MultiTypeAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.model.HomeViewModel;
import com.dscappstudio.kitask.model.HomeViewModelFactory;
import com.dscappstudio.kitask.oparetion.RoutineDao;
import com.dscappstudio.kitask.oparetion.TaskDao;
import com.dscappstudio.kitask.oparetion.TodayTaskDao;
import com.dscappstudio.kitask.profile.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    private TextView tvName, total_count, total_count_tow, total_count_four, total_count_three;
    private ConstraintLayout routing, today_task, complete_task, new_task;
    private TaskDao taskDao;
    private RoutineDao routineDao;
    private TodayTaskDao todayTaskDao;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;

    private MultiTypeAdapter multiTypeAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        // Room Database থেকে TaskDao ইনিশিয়ালাইজ করা
        AppDatabase database = AppDatabase.getInstance(requireContext());
        taskDao = database.taskDao();
        routineDao = database.routineDao();
        todayTaskDao = database.todayTaskDao();

        tvName = homeView.findViewById(R.id.tvName);
        routing = homeView.findViewById(R.id.routing);
        today_task = homeView.findViewById(R.id.today_task);
        complete_task = homeView.findViewById(R.id.complete_task);
        new_task = homeView.findViewById(R.id.new_task);
        total_count = homeView.findViewById(R.id.total_count);
        total_count_tow = homeView.findViewById(R.id.total_count_tow);
        total_count_three = homeView.findViewById(R.id.total_count_three);
        total_count_four = homeView.findViewById(R.id.total_count_four);
        recyclerView = homeView.findViewById(R.id.recyclerView);

        // Initialize the adapter
        multiTypeAdapter = new MultiTypeAdapter(); // Make sure this line exists
        recyclerView.setAdapter(multiTypeAdapter); // Set the adapter to RecyclerView

        // RecyclerView layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ViewModel
        HomeViewModelFactory factory = new HomeViewModelFactory(taskDao, todayTaskDao, routineDao);
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);

        // Observe data
        homeViewModel.getRunningTasks().observe(getViewLifecycleOwner(), tasks -> {
            List<Object> items = new ArrayList<>(tasks);
            multiTypeAdapter.setItems(items);
        });

        homeViewModel.getTodayTasks(getTodayDate()).observe(getViewLifecycleOwner(), todayTasks -> {
            List<Object> items = multiTypeAdapter.getItems(); // Get current items
            items.clear(); // Clear old data
            items.addAll(todayTasks); // Add today tasks
            multiTypeAdapter.setItems(items); // Update adapter
        });

        homeViewModel.getTodayRoutines(getTodayDate()).observe(getViewLifecycleOwner(), routines -> {
            List<Object> items = multiTypeAdapter.getItems(); // Get current items
            items.addAll(routines); // Add today's routines
            multiTypeAdapter.setItems(items); // Update adapter
        });

        // Load profile data from Room Database
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getActivity());
            Profile profile = db.profileDao().getProfile(); // Fetch profile data

            if (profile != null && getActivity() != null) {
                // Use getActivity().runOnUiThread() to update UI
                getActivity().runOnUiThread(() -> {
                    // Update UI with profile data
                    tvName.setText("Hi, " + profile.getName());
                    // Load profile photo if exists
                });
            }
        }).start();

        // Set OnClickListeners for navigation
        new_task.setOnClickListener(v -> NewTaskActivity());
        complete_task.setOnClickListener(v -> CompleteTaskActivity());
        today_task.setOnClickListener(v -> TodayTaskActivity());
        routing.setOnClickListener(v -> RoutineActivity());

        // Task count LiveData কে observe করা
        if (taskDao != null) {
            taskDao.getPendingAndRunningTaskCount().observe(getViewLifecycleOwner(), count -> {
                total_count.setText(String.valueOf(count));
            });
        } else {
            Log.e("HomeFragment", "TaskDao is null");
        }

        // Task count LiveData কে observe করা
        if (taskDao != null) {
            taskDao.getCompleteTaskCount().observe(getViewLifecycleOwner(), count -> {
                total_count_tow.setText(String.valueOf(count));
            });
        } else {
            Log.e("HomeFragment", "TaskDao is null");
        }

        // Routine Count LiveData -----
        if (routineDao != null){
            routineDao.getRoutineCount().observe(getViewLifecycleOwner(), count -> {
                total_count_four.setText(String.valueOf(count));
            });
        } else {
            Log.e("HomeFragment", "RoutineDao is null");
        }

        // Today Task Count LiveData
        if (todayTaskDao != null){
            todayTaskDao.getToadyTaskCount().observe(getViewLifecycleOwner(), count -> {
                total_count_three.setText(String.valueOf(count));
            });
        } else {
            Log.e("HomeFragment", "TodayTaskDao is null");
        }

        return homeView;
    }

    // Activity Task ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void NewTaskActivity() {
        Intent intent = new Intent(getActivity(), NewTaskActivity.class);
        startActivity(intent);
    }

    private void CompleteTaskActivity() {
        Intent intent = new Intent(getActivity(), CompleteTaskActivity.class);
        startActivity(intent);
    }

    private void TodayTaskActivity() {
        Intent intent = new Intent(getActivity(), TodayTaskActivity.class);
        startActivity(intent);
    }

    private void RoutineActivity() {
        Intent intent = new Intent(getActivity(), RoutineActivity.class);
        startActivity(intent);
    }
    // Activity Task ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private String getTodayDate() {
        // Get today's date in required format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
