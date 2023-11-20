package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MessagingActivityTest {

    @Mock
    private DatabaseReference mockedChatsReference;
    @Mock
    private DatabaseReference mockedPushedReference;
    @Mock
    private FirebaseDatabase mockedFirebaseDatabase;
    @Captor
    private ArgumentCaptor<Message> messageCaptor;
    private MessagingActivity messagingActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedFirebaseDatabase.getReference("chats")).thenReturn(mockedChatsReference);
        when(mockedChatsReference.child(anyString())).thenReturn(mockedChatsReference); // Return the mock for any child path
        when(mockedChatsReference.push()).thenReturn(mockedPushedReference); // Return the mock for push()
        messagingActivity = new MessagingActivity(mockedFirebaseDatabase.getReference("chats"));
    }

    @Test
    public void sendMessage_sendsMessageToCorrectFirebasePath() {
        String messageText = "Hello, World!";
        String currentUserId = "user123";
        String otherUserId = "user456";
        String chatId = messagingActivity.createChatId(currentUserId, otherUserId);

        messagingActivity.sendMessage(messageText, currentUserId, chatId);

        // Verify that push() is called to get a new DatabaseReference for the new message
        verify(mockedChatsReference.child(chatId)).push();

        // Verify that setValue is called on the new DatabaseReference with the captured message object
        verify(mockedPushedReference).setValue(messageCaptor.capture());

        // Assert that the captured Message object has the correct properties
        assertEquals("Hello, World!", messageCaptor.getValue().getMessageText());
        assertEquals(currentUserId, messageCaptor.getValue().getMessageUser());
    }
}
