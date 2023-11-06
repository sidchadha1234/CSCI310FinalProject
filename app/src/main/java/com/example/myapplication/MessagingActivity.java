package com.example.myapplication;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
public class MessagingActivity extends AppCompatActivity {
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messagesList;
    private String currentUserEmail; // Add this field
    // You need to initialize currentUserEmail somewhere,
    // maybe from a user object or from an Intent extra.

    private DatabaseReference messagesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // Initialize views...
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messagesList, currentUserEmail);
        recyclerViewMessages.setAdapter(messageAdapter);

        // Setup Firebase...
        messagesReference = FirebaseDatabase.getInstance().getReference().child("Messages");

        // Load messages...
        loadMessages();

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString();
                Message message = new Message(currentUserEmail, otherUserEmail, messageText);//need other user email for this to work (comes from selecitng registerd classeS)
                messagesReference.push().setValue(message);
                editTextMessage.setText("");
            }
        });
    }

    private void loadMessages() {
        messagesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    messagesList.add(message);
                    // Notify the adapter that an item was inserted at the end of the list
                    messageAdapter.notifyItemInserted(messagesList.size() - 1);
                }
            }
//dont worry ab the next 3 funcitons
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}
