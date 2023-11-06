package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
/*
MessagingActivity
Ensure that the currentUserEmail and any other variables like otherUserEmail are properly initialized and assigned before they are used.
The buttonSendMessage's onClickListener references otherUserEmail, which isn't defined in the provided snippet. Make sure it is declared and initialized properly within your activity.

 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private String currentUserID;

    public MessageAdapter(List<Message> messageList, String currentUserID) {
        this.messageList = messageList;
        this.currentUserID = currentUserID;
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
        holder.textViewMessage.setText(message.getMessage());

        // Assuming the Message class has a method getSender() that returns the sender's email.
        if(message.getSenderId().equals(currentUserID)) {
            // Current user is the sender.
            holder.textViewMessage.setGravity(Gravity.END);
            // Customize this message appearance as the sent message.
        } else {
            // Another user sent the message.
            holder.textViewMessage.setGravity(Gravity.START);
            // Customize this message appearance as the received message.
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}