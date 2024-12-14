package com.dscappstudio.kitask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dscappstudio.kitask.R;
import com.dscappstudio.kitask.block_operation.AccessibilityUtils;
import com.dscappstudio.kitask.database.AppDatabase;
import com.dscappstudio.kitask.oparetion.AppBlocked;

import java.util.List;

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.BlockedHolder> {

    private final Context context;
    private final List<AppBlocked> appList;

    public BlockedAdapter(Context context, List<AppBlocked> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public BlockedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_blocked_app, parent, false);
        return new BlockedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedHolder holder, int position) {
        AppBlocked app = appList.get(position);
        holder.appName.setText(app.getAppName());

        // পূর্ববর্তী লিসেনার রিমুভ করুন
        holder.appSwitch.setOnCheckedChangeListener(null);

        // সঠিক অবস্থায় সুইচ সেট করুন
        holder.appSwitch.setChecked(app.isBlocked());

        // নতুন লিসেনার অ্যাড করুন
        holder.appSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            app.setBlocked(isChecked);

            // ডাটাবেজে আপডেট করুন
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                db.appBlockedDao().updateApp(app);

                // অ্যাপ ব্লক বা আনব্লক করুন
                if (isChecked) {
                    AccessibilityUtils.blockApp(context, app.getPackageName());
                } else {
                    AccessibilityUtils.unblockApp(context, app.getPackageName());
                }
            }).start();
        });

    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class BlockedHolder extends RecyclerView.ViewHolder {

        TextView appName;
        Switch appSwitch;

        public BlockedHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appName);
            appSwitch = itemView.findViewById(R.id.switchBlock);
        }
    }

}
