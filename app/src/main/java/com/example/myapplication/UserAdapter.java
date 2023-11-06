package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> userIds;
    private String currentUserId;

    public UserAdapter(Context context, List<String> userIds, String currentUserId) {
        super(context, 0, userIds);
        this.context = context;
        this.userIds = userIds;
        this.currentUserId = currentUserId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        TextView textViewUser = convertView.findViewById(R.id.textViewUser);
        Button buttonMessage = convertView.findViewById(R.id.buttonMessage);

        String userId = getItem(position);
        textViewUser.setText(userId);

        // Check if the userId is for a course or the current user, and hide the message button accordingly
        if (userId.startsWith("Course:") || userId.equals(currentUserId)) {
            buttonMessage.setVisibility(View.GONE);
        } else {
            // It's a regular user, show the message button and set up the click listener
            buttonMessage.setVisibility(View.VISIBLE);
            buttonMessage.setOnClickListener(v -> {
                Intent intent = new Intent(context, MessagingActivity.class);
                intent.putExtra("OTHER_USER_ID", userId);
                context.startActivity(intent);
            });
        }

        return convertView;
    }
}
