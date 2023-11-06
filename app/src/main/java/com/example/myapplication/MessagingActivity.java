package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import java.util.List; // If you are using a List for messagesList

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class MessagingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSendMessage;

    private FirebaseAuth mAuth;
    private DatabaseReference messagesRef;

    private String currentUserId, otherUserId;
    private MessageAdapter messageAdapter;
    private List<Message> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);





        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        // Retrieve the OTHER_USER_ID from the intent
        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");

        // Setup RecyclerView with the adapter
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
// Inside onCreate of MessagingActivity
        messageAdapter = new MessageAdapter(messagesList, currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Initialize the views
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        // Create a unique chat ID, this is a simple way to do it, might not be the best for all scenarios
        String chatId = currentUserId.compareTo(otherUserId) > 0 ? currentUserId + otherUserId : otherUserId + currentUserId;
        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages").child(chatId);
        loadMessages();

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                loadMessages();

            }
        });
        loadMessages();

    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            DatabaseReference newMessageRef = messagesRef.push();
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("senderId", currentUserId);
            messageData.put("receiverId", otherUserId);
            messageData.put("message", messageText);
            messageData.put("timestamp", System.currentTimeMillis());
            newMessageRef.setValue(messageData);

            // Clear the input field after sending the message
            editTextMessage.setText("");
        }
    }

    private void loadMessages() {
        // Here you should add ValueEventListener to messagesRef to listen for changes in the database
        // and update your RecyclerView accordingly. You'll need to create a Message class and a MessageAdapter class
        // for the RecyclerView, similar to what you have with the UserAdapter.

        // This is a placeholder for the actual implementation
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagesList.clear(); // Clear the list to prevent duplicating messages.
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messagesList.add(message);
                    Log.d("MessagingActivity", "Message: " + message);
                }
                messageAdapter.notifyDataSetChanged();
                recyclerViewMessages.scrollToPosition(messagesList.size() - 1);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.w("MessagingActivity", "loadMessages:onCancelled", databaseError.toException());
                // Inform the user that there was a database error (optional)
            }

        });
    }

    // You will also need to create a Message class that corresponds with the message structure in Firebase.
    // Similarly, a MessageAdapter class is needed for the RecyclerView which will use this Message class.
}
