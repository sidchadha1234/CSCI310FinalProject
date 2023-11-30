package com.example.myapplication;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessagingActivity extends AppCompatActivity {
    private static boolean isInTestMode = false;

    private FirebaseListAdapter<Message> adapter;
    private String otherUserId;
    private DatabaseReference chatsRef;

    // Constructor used for testing
    private DatabaseReference mDatabase;


    // Default constructor should be used only when the class is initialized in a real environment
    public MessagingActivity() {
        if (!isInEditMode()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            this.chatsRef = database.getReference("chats");
            this.mDatabase = database.getReference(); // Initialize mDatabase here

        }
    }
    // Overloaded constructor for testing purposes
    public MessagingActivity(DatabaseReference chatsRef) {
        this.chatsRef = chatsRef;
    }

    // ... Rest of your MessagingActivity methods

    private boolean isInEditMode() {
        return isInTestMode;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public static void setTestMode(boolean testMode) {
        isInTestMode = testMode;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatId = createChatId(currentUserId, otherUserId);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            EditText input = findViewById(R.id.input);
            sendMessage(input.getText().toString(), currentUserId, chatId);
            input.setText("");
        });

        displayChatMessages(chatId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    public void sendMessage(String messageText, String userId, String chatId) {
        // Check if the user is blocked
        mDatabase.child("blocks").child(otherUserId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Context should be MessagingActivity.this instead of getContext()
                    Toast.makeText(MessagingActivity.this, "Cannot send message. User is blocked.", Toast.LENGTH_SHORT).show();
                } else {
                    // Send message if not blocked
                    Message message = new Message(messageText, userId);
                    chatsRef.child(chatId).push().setValue(message);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }



    public void displayChatMessages(String chatId) {
        if (adapter == null) {
            FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                    .setQuery(chatsRef.child(chatId), Message.class)
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
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                }
            };
        }

        ListView listOfMessages = findViewById(R.id.list_of_messages);
        listOfMessages.setAdapter(adapter);
    }

    public String createChatId(String currentUserId, String otherUserId) {
        return currentUserId.compareTo(otherUserId) < 0 ? currentUserId + "_" + otherUserId : otherUserId + "_" + currentUserId;
    }

    // Setter for the adapter, visible for testing
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void setAdapter(FirebaseListAdapter<Message> adapter) {
        this.adapter = adapter;
    }
}
