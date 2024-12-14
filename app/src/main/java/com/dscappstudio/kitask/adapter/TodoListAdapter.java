package com.dscappstudio.kitask.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.oparetion.TodoList;
import com.dscappstudio.kitask.oparetion.TodoListDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListHolder> {

    private List<TodoList> todoListList = new ArrayList<>();
    private OnItemClickListener listener;
    private TodoListDao todoListDao;

    // Constructor to initialize Dao
    public TodoListAdapter(TodoListDao todoListDao) {
        this.todoListDao = todoListDao;
    }

    public void setTasks(List<TodoList> lists) {
        this.todoListList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_item, parent, false);
        return new TodoListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        TodoList currentList = todoListList.get(position);
        holder.textViewTitle.setText(currentList.getItem_name());
        holder.textViewPriority.setText(currentList.getItem_priority());

        // Change status icon based on completion status
        if (currentList.isCompleted()) {
            holder.status.setImageResource(R.drawable.complete); // Use a completed icon
        } else {
            holder.status.setImageResource(R.drawable.hourglass); // Use an incomplete icon
        }

        // Convert dueDate to formatted time
        String formattedTime = convertTime(currentList.getDueDate());
        holder.text_view_time.setText(formattedTime);

        holder.text_view_day.setText("Today");
    }

    // Method to convert timestamp to formatted time
    private String convertTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }

    @Override
    public int getItemCount() {
        return todoListList.size();
    }

    public class TodoListHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewPriority;
        private TextView text_view_time;
        private TextView text_view_day;
        private ImageView status;
        private ImageView edit;

        public TodoListHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            text_view_time = itemView.findViewById(R.id.text_view_time);
            text_view_day = itemView.findViewById(R.id.text_view_day);
            status = itemView.findViewById(R.id.status);
            edit = itemView.findViewById(R.id.edit);

            // Status icon click listener
            status.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showStatusDialog(todoListList.get(position), position);
                }
            });

            edit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Delete the item
                    deleteTask(todoListList.get(position), position, itemView);
                }
            });

            // Item click listener
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(todoListList.get(position));
                }
            });
        }

        // Show dialog for status update
        private void showStatusDialog(TodoList todoList, int position) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Update Status");
            builder.setMessage("Mark this task as complete or cancel?");

            // Complete button
            builder.setPositiveButton("Complete", (dialog, which) -> {
                todoList.setCompleted(true); // Mark as complete

                // AsyncTask to update the task in the database
                new UpdateTaskAsync(todoListDao).execute(todoList);

                notifyItemChanged(position); // Update RecyclerView
                dialog.dismiss();
            });

            // Cancel button
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.create().show();
        }
    }

    // AsyncTask for updating the task
    private static class UpdateTaskAsync extends AsyncTask<TodoList, Void, Void> {

        private TodoListDao todoListDao;

        // Constructor to initialize TodoListDao
        UpdateTaskAsync(TodoListDao dao) {
            this.todoListDao = dao;
        }

        @Override
        protected Void doInBackground(TodoList... todoLists) {
            // Update task in background thread
            todoListDao.update(todoLists[0]);
            return null;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(TodoList todoList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Delete the task
    private void deleteTask(TodoList todoList, int position, View itemView) {
        // Create a new thread for the delete operation
        new Thread(() -> {
            todoListDao.delete(todoList);  // Delete from the database

            // Now update the UI thread to modify the RecyclerView
            itemView.post(() -> {
                todoListList.remove(position);  // Remove from the list
                notifyItemRemoved(position);  // Notify the RecyclerView to update
            });
        }).start();
    }


}
