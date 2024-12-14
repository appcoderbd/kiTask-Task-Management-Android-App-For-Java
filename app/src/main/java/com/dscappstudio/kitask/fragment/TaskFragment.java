package com.dscappstudio.kitask.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dscappstudio.kitask.AddToDoListActivity;
import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.adapter.TodoListAdapter;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.TodoList;
import com.dscappstudio.kitask.oparetion.TodoListDao;

import java.util.List;


public class TaskFragment extends Fragment {

    private ConstraintLayout add_item_btn;
    private RecyclerView todoListRecycler;
    private LinearLayout empty_box;

    private TodoListAdapter adapter;
    private TodoListDao todoListDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View taskView = inflater.inflate(R.layout.fragment_task, container, false);

        // Initialize UI Components
        add_item_btn = taskView.findViewById(R.id.add_item_btn);
        todoListRecycler = taskView.findViewById(R.id.todoListRecycler);
        empty_box = taskView.findViewById(R.id.empty_box);



        add_item_btn.setOnClickListener(v -> {p_intent();});

        // Initialize DAO
        todoListDao = AppDatabase.getInstance(getContext()).todoListDao();

        // Setup RecyclerView
        todoListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TodoListAdapter(todoListDao);
        todoListRecycler.setAdapter(adapter);




        // Observe data from DAO
        todoListDao.getAllList().observe(getViewLifecycleOwner(), new Observer<List<TodoList>>() {
            @Override
            public void onChanged(List<TodoList> todoLists) {
                if (todoLists.isEmpty()) {
                    empty_box.setVisibility(View.VISIBLE);
                    todoListRecycler.setVisibility(View.GONE);
                } else {
                    empty_box.setVisibility(View.GONE);
                    todoListRecycler.setVisibility(View.VISIBLE);
                    adapter.setTasks(todoLists);
                }
            }
        });



        return taskView;
    }



    // Private
    private void p_intent(){
        Intent intent = new Intent(getContext(), AddToDoListActivity.class);
        startActivity(intent);
    }
}