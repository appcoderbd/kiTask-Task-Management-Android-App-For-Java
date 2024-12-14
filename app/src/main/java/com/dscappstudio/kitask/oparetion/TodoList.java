package com.dscappstudio.kitask.oparetion;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "Todo_List")
public class TodoList {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String item_name;
    private String item_description;
    private String item_priority;
    private long dueDate;
    private boolean isCompleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_priority() {
        return item_priority;
    }

    public void setItem_priority(String item_priority) {
        this.item_priority = item_priority;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    // Utility Method to Get Formatted Date
    public String getFormattedDueDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(new Date(dueDate));
    }


}
