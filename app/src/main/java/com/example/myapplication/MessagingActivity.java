package com.example.myapplication;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MessagingActivity extends AppCompatActivity {

    private FirebaseListAdapter<Message> adapter;
    private String otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // Retrieve the OTHER_USER_ID from the intent
        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String chatId = createChatId(currentUserId, otherUserId); // Method to create a unique chat ID

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            EditText input = findViewById(R.id.input);
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("chats")
                    .child(chatId)
                    .push()
                    .setValue(new Message(input.getText().toString(), currentUserId)); // Use currentUserId for the message
            input.setText("");
        });
        displayChatMessages(chatId);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the chat
        if(adapter != null) {
            adapter.startListening();
        }
    }


    private void displayChatMessages(String chatId) {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("chats").child(chatId), Message.class)
                .setLayout(R.layout.message_item)
                .build();

        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
    private String createChatId(String currentUserId, String otherUserId) {
        if (currentUserId.compareTo(otherUserId) < 0) {
            return currentUserId + "_" + otherUserId;
        } else {
            return otherUserId + "_" + currentUserId;
        }
    }
}
