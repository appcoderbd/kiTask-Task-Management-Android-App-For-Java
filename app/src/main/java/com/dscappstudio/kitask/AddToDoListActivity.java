package com.dscappstudio.kitask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dscappstudio.kitask.oparetion.TodoList;
import com.dscappstudio.kitask.oparetion.TodoListRepository;

public class AddToDoListActivity extends AppCompatActivity {

    AutoCompleteTextView work_day, ed_priority;
    Toolbar toolbar;
    EditText todo_list_name, todo_description;
    TextView save_btn, cancel_btn;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_list);


        work_day = findViewById(R.id.work_day);
        ed_priority = findViewById(R.id.ed_priority);
        toolbar = findViewById(R.id.toolbar);
        todo_list_name = findViewById(R.id.todo_list_name);
        todo_description = findViewById(R.id.todo_description);
        save_btn = findViewById(R.id.save_btn);
        cancel_btn = findViewById(R.id.cancel_btn);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> {onBackPressed();});


        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        // Work priority
        String[] priority ={"High","Medium","Low"};


        ArrayAdapter<String> day_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, days);
        work_day.setAdapter(day_adapter);
        work_day.setThreshold(0);

        ArrayAdapter<String> priority_adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, priority);
        ed_priority.setAdapter(priority_adapter);
        ed_priority.setThreshold(0);


        // Ensure dropdown appears on touch
        work_day.setOnTouchListener((v, event) -> {
            work_day.showDropDown();
            return false;
        });

        ed_priority.setOnTouchListener((v, event) -> {
            ed_priority.showDropDown();
            return false;
        });

        save_btn.setOnClickListener(v -> {saveData();});
        cancel_btn.setOnClickListener(v -> {onBackPressed();});





    }


    // Save to-do-list Data of Database~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void saveData() {
        String listName = todo_list_name.getText().toString().trim();
        String listDay = work_day.getText().toString().trim();
        String listPriority = ed_priority.getText().toString().trim();
        String listDes = todo_description.getText().toString().trim();

        if (listName.isEmpty() || listDay.isEmpty() || listPriority.isEmpty() || listDes.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        TodoList newTodo = new TodoList();
        newTodo.setItem_name(listName);
        newTodo.setItem_description(listDes);
        newTodo.setItem_priority(listPriority);
        newTodo.setDueDate(System.currentTimeMillis());
        newTodo.setCompleted(false);

        TodoListRepository repository = new TodoListRepository(getApplication());
        repository.insert(newTodo);

        Toast.makeText(this, "To-do item added successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
    // Save to-do-list Data of Database~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}