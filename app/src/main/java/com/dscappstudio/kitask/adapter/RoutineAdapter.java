package com.dscappstudio.kitask.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.Routine;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private List<Routine> routineList;
    private Context context;

    public void setRoutineList(List<Routine> routineList){
        this.routineList = routineList;
        notifyDataSetChanged();
    }

    public RoutineAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RoutineAdapter.RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_item_list, parent, false);
        return new RoutineViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoutineAdapter.RoutineViewHolder holder, int position) {

        Routine routine = routineList.get(position);
        holder.tv_type_name.setText("Type: "+routine.getRoutineName());
        holder.tv_start_time.setText("Start Time: "+routine.getStartTime());
        holder.tv_end_time.setText("End Time: "+routine.getEndTime());
        holder.tv_day.setText("Work Day: "+routine.getWorkDay());
        holder.tv_priority.setText("Priority: "+routine.getWorkPriority());

        holder.clickEvent.setOnClickListener(v -> {
            // Inflate the custom layout
            LayoutInflater inflater = LayoutInflater.from(context); // Use the context
            View dialogView = inflater.inflate(R.layout.routine_item_delete_dialog, null);

            // Initialize the views in the custom layout
            TextView routine_title = dialogView.findViewById(R.id.routine_title);
            LinearLayout i_delete = dialogView.findViewById(R.id.i_delete);
            LinearLayout i_view = dialogView.findViewById(R.id.i_view);
            LinearLayout i_cancel = dialogView.findViewById(R.id.i_cancel);


            // Build the AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme);
            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            // Set button click listener

            routine_title.setText(routine.getRoutineName());
            i_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            i_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // সঠিক কনটেক্সট ব্যবহার করা হচ্ছে
                    AppDatabase db = AppDatabase.getInstance(v.getContext());

                    new Thread(() -> {
                        // ID দিয়ে টাস্ক ডিলিট করা
                        db.routineDao().deleteRoutineById(routine.getId());
                        alertDialog.dismiss();      // ID ব্যবহার করে ডিলিট
                    }).start();
                }
            });

            // Show the dialog
            alertDialog.show();

            alertDialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width: Full screen
                    ViewGroup.LayoutParams.WRAP_CONTENT  // Height: Wrap content
            );
        });


    }

    @Override
    public int getItemCount() {
        return routineList == null ? 0 : routineList.size();
    }

    public class RoutineViewHolder extends RecyclerView.ViewHolder {

        TextView tv_type_name, tv_start_time, tv_end_time, tv_day, tv_priority, tv_work;
        ConstraintLayout clickEvent;
        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_type_name = itemView.findViewById(R.id.tv_type_name);
            tv_start_time = itemView.findViewById(R.id.tv_start_time);
            tv_end_time = itemView.findViewById(R.id.tv_end_time);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_priority = itemView.findViewById(R.id.tv_priority);
            tv_work = itemView.findViewById(R.id.tv_work);
            clickEvent = itemView.findViewById(R.id.clickEvent);
        }
    }


    private void showCustomDialog() {

    }
}
