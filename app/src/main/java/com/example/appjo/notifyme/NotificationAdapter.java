package com.example.appjo.notifyme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<String> notifications = new ArrayList<>();
    private Context context;

    public NotificationAdapter(Context mCtx){
        this.context = mCtx;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String notification = notifications.get(position);
        if (!notification.isEmpty()){
            holder.notificationPackageTextView.setText(notification);
        }
    }

    public void addNotifications(List<String> data){
        notifications = data;
        notifyDataSetChanged();
    }

    public void clearNotifications(){
        final int size = notifications.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                notifications.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public int getItemCount() {
        if (notifications.size() != 0){
            return notifications.size();
        }else {
            return 0;
        }
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView notificationPackageTextView;

        NotificationViewHolder(View view){
            super(view);
            notificationPackageTextView = view.findViewById(R.id.notify_text_view);
        }
    }
}
