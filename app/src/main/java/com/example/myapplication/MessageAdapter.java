package com.example.myapplication;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MessageAdapter  {
   /*

    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        boolean isCurrentUser = message.getSenderId().equals(currentUserId);
        holder.textViewMessage.setText(message.getMessage());

        // Aligning the text to the right or left based on the user.
        holder.textViewMessage.setGravity(isCurrentUser ? Gravity.END : Gravity.START);

        // Set the background resource depending on whether the message is from the current user or not.
        holder.textViewMessage.setBackgroundResource(isCurrentUser ? R.drawable.sender_message_background : R.drawable.receiver_message_background);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        MessageViewHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            // If you have other views like timestamp, you should initialize them here.
        }
    }

    */
}
